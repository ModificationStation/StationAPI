package net.modificationstation.stationapi.api.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.impl.recipe.oredict.ShapedOreDictRecipe;
import net.modificationstation.stationapi.impl.recipe.oredict.ShapelessOreDictRecipe;
import net.modificationstation.stationapi.mixin.recipe.RecipeRegistryAccessor;

import java.util.*;

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
