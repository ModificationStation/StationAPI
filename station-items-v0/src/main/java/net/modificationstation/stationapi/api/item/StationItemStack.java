package net.modificationstation.stationapi.api.item;

import net.minecraft.item.ItemBase;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.registry.RegistryEntry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Util;

public interface StationItemStack extends StationNBT {

    @Override
    default CompoundTag getStationNBT() {
        return Util.assertImpl();
    }

    default RegistryEntry.Reference<ItemBase> getRegistryEntry() {
        return Util.assertImpl();
    }

    default boolean isIn(TagKey<ItemBase> tag) {
        return getRegistryEntry().isIn(tag);
    }
}
