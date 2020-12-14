package net.modificationstation.stationloader.impl.common.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.modificationstation.stationloader.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationloader.api.common.recipe.CraftingRegistry;
import net.modificationstation.stationloader.api.common.recipe.SmeltingRegistry;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.impl.common.item.JsonItemKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class RecipeManager implements net.modificationstation.stationloader.api.common.recipe.RecipeManager, RecipeRegister {

    public RecipeManager() {
        addOrGetRecipeType(Identifier.of("minecraft:crafting_shaped"), (recipe) -> {
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
            CraftingRegistry.INSTANCE.addShapedRecipe(json.getResult().getItemInstance(), keys);
        });
        addOrGetRecipeType(Identifier.of("minecraft:crafting_shapeless"), (recipe) -> {
            JsonCraftingShapeless json;
            try {
                json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonCraftingShapeless.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JsonItemKey[] ingredients = json.getIngredients();
            Object[] itemstacks = new Object[json.getIngredients().length];
            for (int i = 0; i < ingredients.length; i++)
                itemstacks[i] = ingredients[i].getItemInstance();
            CraftingRegistry.INSTANCE.addShapelessRecipe(json.getResult().getItemInstance(), itemstacks);
        });
        addOrGetRecipeType(Identifier.of("minecraft:smelting"), (recipe) -> {
            JsonSmelting json;
            try {
                json = new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonSmelting.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SmeltingRegistry.INSTANCE.addSmeltingRecipe(json.getIngredient().getItemInstance(), json.getResult().getItemInstance());
        });
    }

    @Override
    public void addJsonRecipe(URL recipe) throws IOException {
        addOrGetRecipeType(Identifier.of(new Gson().fromJson(new BufferedReader(new InputStreamReader(recipe.openStream())), JsonRecipeType.class).getType()), null).add(recipe);
    }

    @Override
    public Set<URL> addOrGetRecipeType(Identifier identifier, Consumer<URL> register) {
        String modid = identifier.getModID().toString();
        String type = identifier.getId();
        if (!recipes.containsKey(modid))
            recipes.put(modid, new HashMap<>());
        Map<String, Map.Entry<Consumer<URL>, Set<URL>>> map = recipes.get(modid);
        if (!map.containsKey(type))
            map.put(type, new AbstractMap.SimpleEntry<>(register, new HashSet<>()));
        Map.Entry<Consumer<URL>, Set<URL>> entry = map.get(type);
        if (entry.getKey() == null)
            entry = new AbstractMap.SimpleEntry<>(register, entry.getValue());
        return entry.getValue();
    }

    @Override
    public void registerRecipes(Identifier recipeId) {
        Map.Entry<Consumer<URL>, Set<URL>> recipe = recipes.get(recipeId.getModID().toString()).get(recipeId.getId());
        recipe.getValue().forEach(recipe.getKey());
    }
}
