package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.registry.serial.LegacyIDHolder;
import net.modificationstation.stationapi.api.util.Util;

public interface StationItem extends LegacyIDHolder, ItemConvertible {

    @Override
    default ItemBase asItem() {
        return Util.assertImpl();
    }

    @Override
    default int getLegacyID() {
        return Util.assertImpl();
    }

    default RegistryEntry.Reference<ItemBase> getRegistryEntry() {
        return Util.assertImpl();
    }
}
