package net.modificationstation.stationloader.impl.common.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationloader.mixin.common.accessor.RecipeRegistryAccessor;

public class CraftingRegistry implements net.modificationstation.stationloader.api.common.recipe.CraftingRegistry {

    @Override
    public void setHandler(net.modificationstation.stationloader.api.common.recipe.CraftingRegistry handler) {
        throw new RuntimeException("You shouldn't be able to access this!");
    }

    @Override
    public void addShapedRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).invokeAddShapedRecipe(itemInstance, o);
    }

    @Override
    public void addShapelessRecipe(ItemInstance itemInstance, Object... o) {
        ((RecipeRegistryAccessor) RecipeRegistry.getInstance()).invokeAddShapelessRecipe(itemInstance, o);
    }
}
