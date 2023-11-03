package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.item.Item;

@SuperBuilder
public abstract class ItemEvent extends Event {
    public final Item item;

    @SuperBuilder
    public static class TranslationKeyChanged extends ItemEvent {
        public String translationKeyOverride;
    }
}
