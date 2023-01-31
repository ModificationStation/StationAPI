package net.modificationstation.stationapi.api.nbt;

public interface StationNbtCompound {

    void put(String key, int[] item);

    int[] getIntArray(String key);

    void put(String key, long[] item);

    long[] getLongArray(String key);
}
