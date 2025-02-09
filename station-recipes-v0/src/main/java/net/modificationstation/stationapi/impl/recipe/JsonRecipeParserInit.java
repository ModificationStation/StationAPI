package net.modificationstation.stationapi.impl.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.JsonRecipeParserRegistryEvent;
import net.modificationstation.stationapi.api.item.json.JsonItemKey;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.api.recipe.SmeltingRegistry;
import net.modificationstation.stationapi.api.registry.JsonRecipeParserRegistry;
import net.modificationstation.stationapi.api.registry.JsonRecipesRegistry;
import net.modificationstation.stationapi.api.util.Namespace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class JsonRecipeParserInit {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerJsonRecipeParsers(JsonRecipeParserRegistryEvent event) {
        event.register(Namespace.MINECRAFT)
                .accept("crafting_shaped", JsonRecipeParserInit::parseCraftingShaped)
                .accept("crafting_shapeless", JsonRecipeParserInit::parseCraftingShapeless)
                .accept("smelting", JsonRecipeParserInit::parseSmelting);
    }

    @EventListener
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
            keys[i + 1] = new Gson().fromJson(key.getValue(), JsonItemKey.class).get().map(Function.identity(), Function.identity());
            i += 2;
        }
        CraftingRegistry.addShapedRecipe(json.getResult().getItemStack(), keys);
    }

    private static void parseCraftingShapeless(URL recipe) {
        JsonCraftingShapeless json;
        try {
            json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonCraftingShapeless.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonItemKey[] ingredients = json.getIngredients();
        Object[] stacks = new Object[json.getIngredients().length];
        for (int i = 0; i < ingredients.length; i++)
            stacks[i] = ingredients[i].get().map(Function.identity(), Function.identity());
        try {
            CraftingRegistry.addShapelessRecipe(json.getResult().getItemStack(), stacks);
        } catch (NullPointerException e) {
            throw new RuntimeException("Recipe: " + recipe, e);
        }
    }

    private static void parseSmelting(URL recipe) {
        JsonSmelting json;
        try {
            json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonSmelting.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        json.getIngredient().get().ifLeft(item -> SmeltingRegistry.addSmeltingRecipe(item, json.getResult().getItemStack())).ifRight(tag -> SmeltingRegistry.addSmeltingRecipe(tag, json.getResult().getItemStack()));
    }
}
