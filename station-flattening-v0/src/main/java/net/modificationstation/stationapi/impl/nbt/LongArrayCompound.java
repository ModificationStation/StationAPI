package net.modificationstation.stationapi.impl.nbt;

public interface LongArrayCompound {

    void put(String key, long[] item);

    long[] getLongArray(String key);
}
