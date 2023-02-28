package net.modificationstation.stationapi.api.nbt;

import net.minecraft.util.io.AbstractTag;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NbtIntArray extends AbstractTag {

    public int[] data;

    public NbtIntArray() {}

    public NbtIntArray(int[] value) {
        this.data = value;
    }

    @Override
    public void write(DataOutput out) {
        try {
            out.writeInt(this.data.length);
            for (int l : data)
                out.writeInt(l);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void read(DataInput in) {
        try {
            int length = in.readInt();
            data = new int[length];
            for (int i = 0; i < length; i++)
                data[i] = in.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte getId() {
        return 11;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof NbtIntArray tag && Arrays.equals(data, tag.data));
    }

    @Override
    public NbtIntArray copy() {
        return new NbtIntArray(Arrays.copyOf(data, data.length));
    }
}
