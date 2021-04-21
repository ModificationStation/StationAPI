package net.modificationstation.stationapi.api.event.item;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.item.ItemBase;

@RequiredArgsConstructor
public abstract class ItemEvent extends Event {

    public final ItemBase item;

    public static class TranslationKeyChanged extends ItemEvent {

        public String currentTranslationKey;

        public TranslationKeyChanged(ItemBase item, String currentTranslationKey) {
            super(item);
            this.currentTranslationKey = currentTranslationKey;
        }

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
