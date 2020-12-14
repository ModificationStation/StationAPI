package net.modificationstation.stationloader.api.common.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.common.util.HasHandler;

import java.util.function.Function;

public interface BlockManager extends HasHandler<BlockManager> {

    BlockManager INSTANCE = new BlockManager() {

        private BlockManager handler;

        @Override
        public void setHandler(BlockManager handler) {
            this.handler = handler;
        }

        @Override
        public PlaceableTileEntity getBlockItem(BlockBase block) {
            checkAccess(handler);
            return handler.getBlockItem(block);
        }

        @Override
        public Function<Integer, PlaceableTileEntity> getDefaultBlockItemFactory() {
            checkAccess(handler);
            return handler.getDefaultBlockItemFactory();
        }

        @Override
        public void setDefaultBlockItemFactory(Function<Integer, PlaceableTileEntity> blockItemFactory) {
            checkAccess(handler);
            handler.setDefaultBlockItemFactory(blockItemFactory);
        }
    };

    PlaceableTileEntity getBlockItem(BlockBase block);

    Function<Integer, PlaceableTileEntity> getDefaultBlockItemFactory();

    void setDefaultBlockItemFactory(Function<Integer, PlaceableTileEntity> blockItemFactory);
}
