package net.modificationstation.stationapi.api.item.nbt;

import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;

public interface StationNBT {

    static StationNBT cast(ItemInstance itemInstance) {
        return StationNBT.class.cast(itemInstance);
    }

    CompoundTag getStationNBT();
}
