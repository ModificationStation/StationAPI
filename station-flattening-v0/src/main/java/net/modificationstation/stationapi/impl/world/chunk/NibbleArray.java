package net.modificationstation.stationapi.impl.world.chunk;

import net.minecraft.nbt.NbtByteArray;

public class NibbleArray {
    public final byte[] data;

    public NibbleArray(int capacity) {
        data = new byte[capacity >> 1];
    }

    public int getValue(int index) {
        short value = (short) (data[index >> 1] & 255);
        return (index & 1) == 0 ? value & 15 : value >> 4;
    }

    public void setValue(int index, int value) {
        int index2 = index >> 1;
        short internal = (short) (data[index2] & 255);
        if ((index & 1) == 0) {
            internal = (short) (value | internal & 0xF0);
        }
        else {
            internal = (short) (value << 4 | internal & 15);
        }
        data[index2] = (byte) internal;
    }

    public NbtByteArray toTag() {
        return new NbtByteArray(data);
    }

    public void copyArray(byte[] array) {
        if (array.length != data.length) return;
        System.arraycopy(array, 0, data, 0, data.length);
    }
}
