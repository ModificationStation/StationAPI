package net.modificationstation.stationapi.api.common.event.block;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.common.block.HasMetaBlockItem;
import net.modificationstation.stationapi.api.common.block.HasMetaNamedBlockItem;
import net.modificationstation.stationapi.api.common.event.GameEvent;

import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * When blocks are being registered, this event is called to make it possible to replace the default block item for the current block.
 * @author mine_diver
 * @see HasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory.At
 * @see HasMetaBlockItem
 * @see HasMetaBlockItem.At
 * @see HasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem.At
 */
public interface BlockItemFactoryProvider {

    /**
     * The event instance.
     */
    GameEvent<BlockItemFactoryProvider> EVENT = new GameEvent<>(BlockItemFactoryProvider.class,
            listeners ->
                    (block, currentFactory) -> {
                        for (BlockItemFactoryProvider listener : listeners)
                            currentFactory = listener.getBlockItemFactory(block, currentFactory);
                        return currentFactory;
                    },
            (Consumer<GameEvent<BlockItemFactoryProvider>>) blockItemFactoryProvider ->
                    blockItemFactoryProvider.register((block, currentFactory) -> {
                        Data data = new Data(block, currentFactory);
                        GameEvent.EVENT_BUS.post(data);
                        return data.getCurrentFactory();
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
    @Getter
    final class Data extends GameEvent.Data<BlockItemFactoryProvider> {

        /**
         * Current block.
         */
        private final BlockBase block;

        /**
         * Current factory that's going to be executed to get block item instance.
         */
        @Setter
        private IntFunction<PlaceableTileEntity> currentFactory;

        private Data(BlockBase block, IntFunction<PlaceableTileEntity> currentFactory) {
            super(EVENT);
            this.block = block;
            this.currentFactory = currentFactory;
        }
    }
}
