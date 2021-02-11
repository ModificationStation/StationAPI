package net.modificationstation.stationapi.api.common.event.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.block.*;
import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * When blocks are being registered, this event is called to make it possible to replace the default block item for the current block.
 * @author mine_diver
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 */
public interface BlockItemFactoryProvider {

    /**
     * The event instance.
     */
    GameEventOld<BlockItemFactoryProvider> EVENT = new GameEventOld<>(BlockItemFactoryProvider.class,
            listeners ->
                    (block, currentFactory) -> {
                        for (BlockItemFactoryProvider listener : listeners)
                            currentFactory = listener.getBlockItemFactory(block, currentFactory);
                        return currentFactory;
                    },
            (Consumer<GameEventOld<BlockItemFactoryProvider>>) blockItemFactoryProvider ->
                    blockItemFactoryProvider.register((block, currentFactory) -> {
                        Data data = new Data(block, currentFactory);
                        GameEventOld.EVENT_BUS.post(data);
                        return data.currentFactory;
                    })
    );

    /**
     * The event function.
     * @param block current block.
     * @param currentFactory current factory that's going to be executed to get block item instance.
     * @return new or current factory.
     */
    IntFunction<PlaceableTileEntity> getBlockItemFactory(BlockBase block, IntFunction<PlaceableTileEntity> currentFactory);

    /**
     * The event data used by EventBus.
     */
    final class Data extends GameEventOld.Data<BlockItemFactoryProvider> {

        /**
         * Current block.
         */
        public final BlockBase block;

        /**
         * Current factory that's going to be executed to get block item instance.
         */
        public IntFunction<PlaceableTileEntity> currentFactory;

        private Data(BlockBase block, IntFunction<PlaceableTileEntity> currentFactory) {
            super(EVENT);
            this.block = block;
            this.currentFactory = currentFactory;
        }
    }
}
