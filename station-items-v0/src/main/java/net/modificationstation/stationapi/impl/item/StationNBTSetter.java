package net.modificationstation.stationapi.impl.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public interface StationNBTSetter {
    static StationNBTSetter cast(ItemStack stack) {
        return StationNBTSetter.class.cast(stack);
    }

    void setStationNbt(NbtCompound stationNbt);
}
