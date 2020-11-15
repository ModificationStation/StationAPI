package net.modificationstation.stationloader.impl.common.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.tileentity.TileEntityFurnace;
import net.modificationstation.stationloader.api.common.util.UnsafeProvider;
import net.modificationstation.stationloader.mixin.common.accessor.SmeltingRecipeRegistryAccessor;
import net.modificationstation.stationloader.mixin.common.accessor.TileEntityFurnaceAccessor;

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
        return DUMMY_TILE_ENTITY_FURNACE.invokeGetFuelTime(itemInstance);
    }

    private static final TileEntityFurnaceAccessor DUMMY_TILE_ENTITY_FURNACE;
    static {
        try {
            DUMMY_TILE_ENTITY_FURNACE = (TileEntityFurnaceAccessor) UnsafeProvider.INSTANCE.getUnsafe().allocateInstance(TileEntityFurnace.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
