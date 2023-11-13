package net.modificationstation.stationapi.api.nbt;

import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.util.Util;

public interface StationNbtCompound extends StationNbtElement {
    default void put(String key, int[] item) {
        Util.assertImpl();
    }

    default int[] getIntArray(String key) {
        return Util.assertImpl();
    }

    default void put(String key, long[] item) {
        Util.assertImpl();
    }

    default long[] getLongArray(String key) {
        return Util.assertImpl();
    }

    @Override
    default NbtCompound copy() {
        return Util.assertImpl();
    }
}
