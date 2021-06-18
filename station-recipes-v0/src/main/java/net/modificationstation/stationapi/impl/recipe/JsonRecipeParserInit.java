package net.modificationstation.stationapi.impl.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.JsonRecipeParserRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.JsonRecipeParserRegistry;
import net.modificationstation.stationapi.api.registry.JsonRecipesRegistry;
import net.modificationstation.stationapi.api.template.item.JsonItemKey;

import java.io.*;
import java.net.*;
import java.util.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class JsonRecipeParserInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerJsonRecipeParsers(JsonRecipeParserRegistryEvent event) {
        event.registry.register(Identifier.of("crafting_shaped"), JsonRecipeParserInit::parseCraftingShaped);
        event.registry.register(Identifier.of("crafting_shapeless"), JsonRecipeParserInit::parseCraftingShapeless);
        event.registry.register(Identifier.of("smelting"), JsonRecipeParserInit::parseSmelting);
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void parseAndRegisterRecipe(RecipeRegisterEvent event) {
        JsonRecipeParserRegistry.INSTANCE.get(event.recipeId).ifPresent(recipeParser -> JsonRecipesRegistry.INSTANCE.get(event.recipeId).ifPresent(recipes -> recipes.forEach(recipeParser)));
    }

    private static void parseCraftingShaped(URL recipe) {
        JsonElement rawJson;
        try {
            rawJson = JsonParser.parseReader(new BufferedReader(new InputStreamReader(recipe.openStream())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonCraftingShaped json = new Gson().fromJson(rawJson, JsonCraftingShaped.class);
        Set<Map.Entry<String, JsonElement>> rawKeys = rawJson.getAsJsonObject().getAsJsonObject("key").entrySet();
        String[] pattern = json.getPattern();
        Object[] keys = new Object[rawKeys.size() * 2 + pattern.length];
        int i = 0;
        for (; i < pattern.length; i++)
            keys[i] = pattern[i];
        for (Map.Entry<String, JsonElement> key : rawKeys) {
            keys[i] = key.getKey().charAt(0);
            keys[i + 1] = new Gson().fromJson(key.getValue(), JsonItemKey.class).getItemInstance();
            i += 2;
        }
        CraftingRegistry.addShapedRecipe(json.getResult().getItemInstance(), keys);
    }

    private static void parseCraftingShapeless(URL recipe) {
        JsonCraftingShapeless json;
        try {
            json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonCraftingShapeless.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonItemKey[] ingredients = json.getIngredients();
        Object[] iteminstances = new Object[json.getIngredients().length];
        for (int i = 0; i < ingredients.length; i++)
            iteminstances[i] = ingredients[i].getItemInstance();
        CraftingRegistry.addShapelessRecipe(json.getResult().getItemInstance(), iteminstances);
    }

    private static void parseSmelting(URL recipe) {
        JsonSmelting json;
        try {
            json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonSmelting.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SmeltingRegistry.addSmeltingRecipe(json.getIngredient().getItemInstance(), json.getResult().getItemInstance());
    }
}
