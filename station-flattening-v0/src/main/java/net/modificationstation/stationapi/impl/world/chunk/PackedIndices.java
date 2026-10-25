package net.modificationstation.stationapi.impl.world.chunk;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

/**
 * A section's palette indices as {@link ChunkSection} stores them: {@code bits} per cell, cell {@code c} at bit
 * {@code c * bits}, little-endian, so any index can straddle bytes and words. Reading one is a single unaligned 4-byte
 * load at byte {@code bit / 8}, shifted by {@code bit % 8}, the same cost at every width; 4 bytes of padding past the
 * last index let the last window fit.
 */
final class PackedIndices {
    static final int VOLUME = 16 * 16 * 16;
    /** reads and writes one cell's index */
    static final VarHandle INT_LE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);
    /** writes and copies whole words of the indices, which never reach the padding */
    static final VarHandle LONG_LE = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);

    private PackedIndices() {}

    static byte[] alloc(int bits) {
        return new byte[(VOLUME * bits + 7) / 8 + 4];
    }

    /** {@return the narrowest width that holds {@code paletteSize} distinct indices} */
    static int widthFor(int paletteSize) {
        return paletteSize <= 1 ? 0 : 32 - Integer.numberOfLeadingZeros(paletteSize - 1);
    }

    static int get(byte[] data, int bits, int cell) {
        int bit = cell * bits;
        return ((int) INT_LE.get(data, bit >>> 3) >>> (bit & 7)) & ((1 << bits) - 1);
    }

    static void set(byte[] data, int bits, int cell, int index) {
        if (bits == 0) return;
        int bit = cell * bits;
        int offset = bit >>> 3;
        int shift = bit & 7;
        int mask = ((1 << bits) - 1) << shift;
        int window = (int) INT_LE.get(data, offset);
        INT_LE.set(data, offset, (window & ~mask) | ((index << shift) & mask));
    }

    /**
     * Writes every cell's index, as {@link #set} would, but one 8-byte store per 64 bits instead of a read-modify-write
     * per cell. Each index goes in above the bits already in the word; one that crosses into the next word starts it
     * with its remaining high bits.
     */
    static void write(byte[] data, int bits, int[] indices) {
        if (bits == 0) return;
        long word = 0;
        int filled = 0, offset = 0;
        for (int cell = 0; cell < VOLUME; cell++) {
            long index = indices[cell];
            word |= index << filled;
            filled += bits;
            if (filled >= 64) {
                LONG_LE.set(data, offset, word);
                offset += 8;
                filled -= 64;
                // the bits of index that didn't fit
                word = filled == 0 ? 0 : index >>> (bits - filled);
            }
        }
        // 4096 cells fill whole longs at any width, so nothing is left over
    }
}
