package net.modificationstation.stationapi.api.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.impl.recipe.tag.ShapedTagRecipe;
import net.modificationstation.stationapi.impl.recipe.tag.ShapelessTagRecipe;
import net.modificationstation.stationapi.mixin.recipe.RecipeRegistryAccessor;

import java.util.*;

public class CraftingRegistry {

    @API
    public static void addShapedRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).stationapi$addShapedRecipe(itemInstance, o);
    }

    @API
    public static void addShapelessRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).stationapi$addShapelessRecipe(itemInstance, o);
    }

    @API
    public static void addShapelessOreDictRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).stationapi$getRecipes().add(new ShapelessTagRecipe(itemInstance, Arrays.asList(o)));
    }

    @API
    public static void addShapedOreDictRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).stationapi$getRecipes().add(new ShapedTagRecipe(itemInstance, Arrays.asList(o)));
    }
}
