package net.modificationstation.stationapi.impl.item.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public interface StationNBTSetter {

    static StationNBTSetter cast(ItemStack itemInstance) {
        return StationNBTSetter.class.cast(itemInstance);
    }

    void setStationNBT(NbtCompound stationNBT);
}
