package net.modificationstation.stationloader.impl.common.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.tileentity.TileEntityFurnace;
import net.modificationstation.stationloader.api.common.event.block.TileEntityRegister;
import net.modificationstation.stationloader.api.common.util.UnsafeProvider;
import net.modificationstation.stationloader.impl.common.util.OreDict;
import net.modificationstation.stationloader.mixin.common.accessor.SmeltingRecipeRegistryAccessor;
import net.modificationstation.stationloader.mixin.common.accessor.TileEntityFurnaceAccessor;

import java.util.function.BiConsumer;

public class SmeltingRegistry implements net.modificationstation.stationloader.api.common.recipe.SmeltingRegistry, TileEntityRegister {

    private TileEntityFurnaceAccessor DUMMY_TILE_ENTITY_FURNACE;

    @Override
    public void addSmeltingRecipe(int input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @Override
    public void addSmeltingRecipe(ItemInstance input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @Override
    public void addOreDictSmeltingRecipe(String input, ItemInstance output) {
        ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().put(input, output);
    }

    @Override
    public ItemInstance getResultFor(ItemInstance input) {
        for (Object o : ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().keySet()) {
            if (o instanceof ItemInstance && input.isEqualIgnoreFlags((ItemInstance) o) || o instanceof String && OreDict.INSTANCE.matches((String) o, input))
                return ((SmeltingRecipeRegistryAccessor) SmeltingRecipeRegistry.getInstance()).getRecipes().get(o);
        }
        return SmeltingRecipeRegistry.getInstance().getResult(input.getType().id);
    }

    @Override
    public int getFuelTime(ItemInstance itemInstance) {
        if (DUMMY_TILE_ENTITY_FURNACE == null)
            throw new RuntimeException("Accessed Lnet/modificationstation/stationloader/api/common/recipe/SmeltingRegistry;getFuelTime(Lnet/minecraft/item/ItemInstance;)I too early!");
        else
            return DUMMY_TILE_ENTITY_FURNACE.invokeGetFuelTime(itemInstance);
    }

    @Override
    public void registerTileEntities(BiConsumer<Class<? extends TileEntityBase>, String> register) {
        try {
            DUMMY_TILE_ENTITY_FURNACE = (TileEntityFurnaceAccessor) UnsafeProvider.INSTANCE.getUnsafe().allocateInstance(TileEntityFurnace.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
