package net.modificationstation.stationloader.impl.common.recipe;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationloader.api.common.event.item.Fuel;
import net.modificationstation.stationloader.mixin.common.accessor.SmeltingRecipeRegistryAccessor;

public class SmeltingRegistry implements net.modificationstation.stationloader.api.common.recipe.SmeltingRegistry {

    @Override
    public void addSmeltingRecipe(int input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @Override
    public void addSmeltingRecipe(ItemInstance input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @Override
    public ItemInstance getResultFor(ItemInstance input) {
        for (Object o : ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().keySet()) {
            if (o instanceof ItemInstance && input.isEqualIgnoreFlags((ItemInstance) o))
                return ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().get(o);
        }
        return SmeltingRecipeRegistry.getInstance().getResult(input.getType().id);
    }

    @Override
    public int getFuelTime(ItemInstance itemInstance) {
        if (itemInstance == null) {
            return 0;
        } else if (itemInstance.getType() instanceof Fuel) {
            return ((Fuel) itemInstance.getType()).getFuelTime(itemInstance);
        } else {
            int itemId = itemInstance.getType().id;
            if (itemId < 256 && BlockBase.BY_ID[itemId].material == Material.WOOD) {
                return 300;
            } else if (itemId == ItemBase.stick.id) {
                return 100;
            } else if (itemId == ItemBase.coal.id) {
                return 1600;
            } else if (itemId == ItemBase.lavaBucket.id) {
                return 20000;
            } else if (itemId == BlockBase.SAPLING.id) {
                return 100;
            } else {
                return 0;
            }
        }
    }
}
