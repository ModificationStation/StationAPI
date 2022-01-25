package net.modificationstation.stationapi.api.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.mixin.recipe.RecipeRegistryAccessor;

public class CraftingRegistry {

    @API
    public static void addShapedRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).stationapi$addShapedRecipe(itemInstance, o);
    }

    @API
    public static void addShapelessRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).stationapi$addShapelessRecipe(itemInstance, o);
    }
}
