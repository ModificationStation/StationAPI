package net.minecraft.util.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LongArrayTag extends AbstractTag {

    public long[] data;

    public LongArrayTag() {}

    public LongArrayTag(long[] value) {
        this.data = value;
    }

    @Override
    void write(DataOutput out) {
        try {
            out.writeInt(this.data.length);
            for (long l : data)
                out.writeLong(l);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    void read(DataInput in) {
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
    public String toString() {
        return "[" + this.data.length + " longs]";
    }
}
