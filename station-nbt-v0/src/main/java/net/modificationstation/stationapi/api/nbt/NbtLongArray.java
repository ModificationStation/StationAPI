package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.AbstractTag;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NbtLongArray extends AbstractTag {

    public long[] data;

    public NbtLongArray() {}

    public NbtLongArray(long[] value) {
        this.data = value;
    }

    @Override
    public void write(DataOutput out) {
        try {
            out.writeInt(this.data.length);
            for (long l : data)
                out.writeLong(l);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void read(DataInput in) {
        try {
            int length = in.readInt();
            data = new long[length];
            for (int i = 0; i < length; i++)
                data[i] = in.readLong();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte getId() {
        return 12;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtLongArray tag && Arrays.equals(data, tag.data));
    }

    @Override
    public String toString() {
        return "[" + this.data.length + " longs]";
    }

    @Override
    public NbtLongArray copy() {
        return new NbtLongArray(Arrays.copyOf(data, data.length));
    }
}
