package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.item.ItemBase;

@SuperBuilder
public abstract class ItemEvent extends Event {

    public final ItemBase item;

    @SuperBuilder
    public static class TranslationKeyChanged extends ItemEvent {

        public String translationKeyOverride;

        @Override
        protected int getEventID() {
            return ID;
        }

        public static final int ID = NEXT_ID.incrementAndGet();
    }
}
