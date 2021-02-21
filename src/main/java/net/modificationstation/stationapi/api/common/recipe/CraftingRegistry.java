package net.modificationstation.stationapi.api.common.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.common.util.API;
import net.modificationstation.stationapi.impl.common.recipe.ShapedOreDictRecipe;
import net.modificationstation.stationapi.impl.common.recipe.ShapelessOreDictRecipe;
import net.modificationstation.stationapi.mixin.common.accessor.RecipeRegistryAccessor;

import java.util.Arrays;

public class CraftingRegistry {

    @API
    public static void addShapedRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).invokeAddShapedRecipe(itemInstance, o);
    }

    @API
    public static void addShapelessRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).invokeAddShapelessRecipe(itemInstance, o);
    }

    @API
    public static void addShapelessOreDictRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).getRecipes().add(new ShapelessOreDictRecipe(itemInstance, Arrays.asList(o)));
    }

    @API
    public static void addShapedOreDictRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).getRecipes().add(new ShapedOreDictRecipe(itemInstance, Arrays.asList(o)));
    }
}
