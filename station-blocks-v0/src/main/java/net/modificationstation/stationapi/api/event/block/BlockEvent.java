package net.modificationstation.stationapi.api.event.block;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.block.BlockBase;

@RequiredArgsConstructor
public abstract class BlockEvent extends Event {

    public final BlockBase block;

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
