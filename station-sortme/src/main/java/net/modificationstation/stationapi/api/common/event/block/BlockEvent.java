package net.modificationstation.stationapi.api.common.event.block;

import lombok.RequiredArgsConstructor;
import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.block.HasMetaBlockItem;
import net.modificationstation.stationapi.api.block.HasMetaNamedBlockItem;
import net.modificationstation.stationapi.api.block.IHasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.block.IHasMetaBlockItem;
import net.modificationstation.stationapi.api.block.IHasMetaNamedBlockItem;
import net.mine_diver.unsafeevents.Event;

import java.util.function.*;

@RequiredArgsConstructor
public abstract class BlockEvent extends Event {

    public final BlockBase block;

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
    public static class ItemFactory extends BlockEvent {

        /**
         * Current factory that's going to be executed to get block item instance.
         */
        public IntFunction<Block> currentFactory;

        public ItemFactory(BlockBase block, IntFunction<Block> currentFactory) {
            super(block);
            this.currentFactory = currentFactory;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }

    public static class TranslationKeyChanged extends BlockEvent {

        public String currentTranslationKey;

        public TranslationKeyChanged(BlockBase block, String currentTranslationKey) {
            super(block);
            this.currentTranslationKey = currentTranslationKey;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
