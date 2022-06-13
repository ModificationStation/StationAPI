package net.modificationstation.stationapi.impl.nbt;

public interface StationCompoundTag {

    void put(String key, int[] item);

    int[] getIntArray(String key);

    void put(String key, long[] item);

    long[] getLongArray(String key);
}
