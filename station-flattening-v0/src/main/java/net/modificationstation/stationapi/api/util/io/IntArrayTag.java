package net.modificationstation.stationapi.api.util.io;

import net.minecraft.util.io.AbstractTag;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntArrayTag extends AbstractTag {

    public int[] data;

    public IntArrayTag() {}

    public IntArrayTag(int[] value) {
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
}
