package net.modificationstation.stationloader.impl.common.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;

import java.util.function.Function;

public class BlockManager implements net.modificationstation.stationloader.api.common.block.BlockManager {

    private Function<Integer, PlaceableTileEntity> defaultBlockItemFactory = PlaceableTileEntity::new;

    @Override
    public PlaceableTileEntity getBlockItem(BlockBase block) {
        int shiftedID = block.id - BlockBase.BY_ID.length;
        if (block instanceof BlockItemProvider)
            return ((BlockItemProvider) block).getBlockItem(shiftedID);
        else
            return defaultBlockItemFactory.apply(shiftedID);
    }

    @Override
    public Function<Integer, PlaceableTileEntity> getDefaultBlockItemFactory() {
        return defaultBlockItemFactory;
    }

    @Override
    public void setDefaultBlockItemFactory(Function<Integer, PlaceableTileEntity> blockItemFactory) {
        defaultBlockItemFactory = blockItemFactory;
    }
}
