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
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.getBlockItem(block);
        }

        @Override
        public void setDefaultBlockItemFactory(Function<Integer, PlaceableTileEntity> blockItemFactory) {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                handler.setDefaultBlockItemFactory(blockItemFactory);
        }

        @Override
        public Function<Integer, PlaceableTileEntity> getDefaultBlockItemFactory() {
            if (handler == null)
                throw new RuntimeException("Accessed StationLoader too early!");
            else
                return handler.getDefaultBlockItemFactory();
        }
    };

    PlaceableTileEntity getBlockItem(BlockBase block);

    void setDefaultBlockItemFactory(Function<Integer, PlaceableTileEntity> blockItemFactory);

    Function<Integer, PlaceableTileEntity> getDefaultBlockItemFactory();
}
