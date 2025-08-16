package net.modificationstation.stationapi.api.network;

import java.nio.ByteBuffer;

public class VarInt {
    private static final int MAX_VARINT_SIZE = 5;
    private static final int MAX_VARLONG_SIZE = 10;
    private static final int DATA_BITS_MASK = 127;
    private static final int CONTINUATION_BIT_MASK = 128;
    private static final int DATA_BITS_PER_BYTE = 7;

    public static boolean hasContinuationBit(byte part) {
        return (part & CONTINUATION_BIT_MASK) == CONTINUATION_BIT_MASK;
    }

    public static int getVarIntSize(int value) {
        int result = 0;
        do {
            result++;
            value >>>= DATA_BITS_PER_BYTE;
        } while (value != 0);
        return result;
    }

    public static void writeVarInt(int value, ByteBuffer buf) {
        while ((value & -CONTINUATION_BIT_MASK) != 0) {
            buf.put((byte) (value & DATA_BITS_MASK | CONTINUATION_BIT_MASK));
            value >>>= DATA_BITS_PER_BYTE;
        }

        buf.put((byte) value);
    }

    public static int readVarInt(ByteBuffer buf) {
        int result = 0;
        int offset = 0;

        byte bit;
        do {
            bit = buf.get();
            result |= (bit & DATA_BITS_MASK) << offset++ * DATA_BITS_PER_BYTE;
            if (offset > MAX_VARINT_SIZE) {
                throw new RuntimeException("VarInt too big");
            }
        } while (hasContinuationBit(bit));

        return result;
    }

    public static int getVarLongSize(long value) {
        int result = 0;
        do {
            result++;
            value >>>= DATA_BITS_PER_BYTE;
        } while (value != 0);
        return result;
    }

    public static void writeVarLong(long value, ByteBuffer buf) {
        while ((value & -CONTINUATION_BIT_MASK) != 0L) {
            buf.put((byte) ((value & DATA_BITS_MASK) | CONTINUATION_BIT_MASK));
            value >>>= DATA_BITS_PER_BYTE;
        }

        buf.put((byte) value);
    }

    public static long readVarLong(ByteBuffer buf) {
        long result = 0L;
        int offset = 0;

        byte bit;
        do {
            bit = buf.get();
            result |= (long) (bit & DATA_BITS_MASK) << offset++ * DATA_BITS_PER_BYTE;
            if (offset > MAX_VARLONG_SIZE) {
                throw new RuntimeException("VarLong too big");
            }
        } while (hasContinuationBit(bit));

        return result;
    }
}
