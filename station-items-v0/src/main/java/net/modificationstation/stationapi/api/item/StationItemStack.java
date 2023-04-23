package net.modificationstation.stationapi.api.item;

import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.item.nbt.StationNBT;
import net.modificationstation.stationapi.api.util.Util;

public interface StationItemStack extends StationNBT {

    @Override
    default CompoundTag getStationNBT() {
        return Util.assertImpl();
    }
}
