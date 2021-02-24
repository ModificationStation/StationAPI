package net.modificationstation.stationapi.api.common.event.item;

import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor
public class ItemEvent extends Event {

    public final ItemBase item;

    public static class TranslationKeyChanged extends ItemEvent {

        public String currentTranslationKey;

        public TranslationKeyChanged(ItemBase item, String currentTranslationKey) {
            super(item);
            this.currentTranslationKey = currentTranslationKey;
        }
    }
}
