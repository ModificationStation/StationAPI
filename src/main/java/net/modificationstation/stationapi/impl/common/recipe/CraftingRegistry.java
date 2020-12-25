package net.modificationstation.stationapi.impl.common.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.mixin.common.accessor.RecipeRegistryAccessor;

import java.util.Arrays;

public class CraftingRegistry implements net.modificationstation.stationapi.api.common.recipe.CraftingRegistry {

    @Override
    public void addShapedRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).invokeAddShapedRecipe(itemInstance, o);
    }

    @Override
    public void addShapelessRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).invokeAddShapelessRecipe(itemInstance, o);
    }

    @Override
    public void addShapelessOreDictRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).getRecipes().add(new ShapelessOreDictRecipe(itemInstance, Arrays.asList(o)));
    }

    @Override
    public void addShapedOreDictRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).getRecipes().add(new ShapedOreDictRecipe(itemInstance, Arrays.asList(o)));
    }

}
