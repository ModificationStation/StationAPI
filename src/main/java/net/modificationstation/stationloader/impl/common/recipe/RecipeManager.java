package net.modificationstation.stationloader.impl.common.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.modificationstation.stationloader.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationloader.api.common.recipe.CraftingRegistry;
import net.modificationstation.stationloader.api.common.recipe.SmeltingRegistry;
import net.modificationstation.stationloader.impl.common.item.JsonItemKey;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

public class RecipeManager implements net.modificationstation.stationloader.api.common.recipe.RecipeManager, RecipeRegister {

    public RecipeManager() {
        addOrGetRecipeType("minecraft:crafting_shaped", (recipe) -> {
            JsonElement rawJson = JsonParser.parseReader(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(recipe))));
            JsonCraftingShaped json = new Gson().fromJson(rawJson, JsonCraftingShaped.class);
            Set<Map.Entry<String, JsonElement>> rawKeys = rawJson.getAsJsonObject().getAsJsonObject("key").entrySet();
            String[] pattern = json.getPattern();
            Object[] keys = new Object[rawKeys.size() * 2 + pattern.length];
            int i = 0;
            for (;i < pattern.length; i++)
                keys[i] = pattern[i];
            for (Map.Entry<String, JsonElement> key : rawKeys) {
                keys[i] = key.getKey().charAt(0);
                keys[i + 1] = new Gson().fromJson(key.getValue(), JsonItemKey.class).getItemInstance();
                i+=2;
            }
            CraftingRegistry.INSTANCE.addShapedRecipe(json.getResult().getItemInstance(), keys);
        });
        addOrGetRecipeType("minecraft:crafting_shapeless", (recipe) -> {
            JsonCraftingShapeless json = new Gson().fromJson(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(recipe))), JsonCraftingShapeless.class);
            JsonItemKey[] ingredients = json.getIngredients();
            Object[] itemstacks = new Object[json.getIngredients().length];
            for (int i = 0; i < ingredients.length; i++)
                itemstacks[i] = ingredients[i].getItemInstance();
            CraftingRegistry.INSTANCE.addShapelessRecipe(json.getResult().getItemInstance(), itemstacks);
        });
        addOrGetRecipeType("minecraft:smelting", (recipe) -> {
            JsonSmelting json = new Gson().fromJson(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(recipe))), JsonSmelting.class);
            SmeltingRegistry.INSTANCE.addSmeltingRecipe(json.getIngredient().getItemInstance(), json.getResult().getItemInstance());
        });
    }

    @Override
    public void addJsonRecipe(String recipe) {
        addOrGetRecipeType(new Gson().fromJson(new BufferedReader(new InputStreamReader(RecipeManager.class.getResourceAsStream(recipe))), JsonRecipeType.class).getType(), null).add(recipe);
    }

    @Override
    public Set<String> addOrGetRecipeType(String type, Consumer<String> register) {
        String modid = type.split(":")[0];
        type = type.substring(modid.length() + 1);
        if (!recipes.containsKey(modid))
            recipes.put(modid, new HashMap<>());
        Map<String, Map.Entry<Consumer<String>, Set<String>>> map = recipes.get(modid);
        if (!map.containsKey(type))
            map.put(type, new AbstractMap.SimpleEntry<>(register, new HashSet<>()));
        return map.get(type).getValue();
    }

    @Override
    public void registerRecipes(String recipeType) {
        String modid = recipeType.split(":")[0];
        Map.Entry<Consumer<String>, Set<String>> recipe = recipes.get(modid).get(recipeType.substring(modid.length() + 1));
        recipe.getValue().forEach(recipe.getKey());
    }
}
