package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.RemappableRawIdHolder;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;

public interface StationItem extends RemappableRawIdHolder, ItemConvertible {

    @Override
    default ItemBase asItem() {
        return Util.assertImpl();
    }

    @Override
    @ApiStatus.Internal
    default void setRawId(int rawId) {
        Util.assertImpl();
    }

    default RegistryEntry.Reference<ItemBase> getRegistryEntry() {
        return Util.assertImpl();
    }

    default ItemBase setTranslationKey(ModID modID, String translationKey) {
        return Util.assertImpl();
    }

    default ItemBase setTranslationKey(Identifier translationKey) {
        return Util.assertImpl();
    }
}
