package net.modificationstation.stationapi.impl.item.nbt;

import net.minecraft.item.ItemInstance;
import net.minecraft.util.io.CompoundTag;

public interface StationNBTSetter {

    static StationNBTSetter cast(ItemInstance itemInstance) {
        return StationNBTSetter.class.cast(itemInstance);
    }

    void setStationNBT(CompoundTag stationNBT);
}
