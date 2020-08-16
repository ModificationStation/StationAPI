package net.modificationstation.stationloader.common.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationloader.mixin.common.RecipeRegistryAccessor;

public class CraftingRegistry {

    public static void addShapedRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).invokeAddShapedRecipe(itemInstance, o);
    }

    public static void addShapelessRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).invokeAddShapelessRecipe(itemInstance, o);
    }
}
