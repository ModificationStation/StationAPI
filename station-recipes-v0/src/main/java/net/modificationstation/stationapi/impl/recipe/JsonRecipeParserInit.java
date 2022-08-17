package net.modificationstation.stationapi.impl.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.JsonRecipeParserRegistryEvent;
import net.modificationstation.stationapi.api.item.json.JsonItemKey;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.registry.JsonRecipeParserRegistry;
import net.modificationstation.stationapi.api.registry.JsonRecipesRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class JsonRecipeParserInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerJsonRecipeParsers(JsonRecipeParserRegistryEvent event) {
        Registry.register(event.registry, of("crafting_shaped"), JsonRecipeParserInit::parseCraftingShaped);
        Registry.register(event.registry, of("crafting_shapeless"), JsonRecipeParserInit::parseCraftingShapeless);
        Registry.register(event.registry, of("smelting"), JsonRecipeParserInit::parseSmelting);
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void parseAndRegisterRecipe(RecipeRegisterEvent event) {
        Consumer<URL> jsonRecipeParser = JsonRecipeParserRegistry.INSTANCE.get(event.recipeId);
        Set<URL> jsonRecipes = JsonRecipesRegistry.INSTANCE.get(event.recipeId);
        if (jsonRecipeParser != null && jsonRecipes != null)
            jsonRecipes.forEach(jsonRecipeParser);
    }

    private static void parseCraftingShaped(URL recipe) {
        JsonElement rawJson;
        try {
            rawJson = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonElement.class);
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
