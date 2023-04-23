package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Util;

public interface StationItem {

    default ItemBase setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default ItemBase setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }
}
