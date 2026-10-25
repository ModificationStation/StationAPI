package net.modificationstation.stationapi.impl.world.chunk;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * A 4-bit value for each of a section's 4096 cells, stored per 16x16 layer: a layer whose cells all hold the same
 * value keeps just that value, and only the others take 128 bytes.
 *
 * <p>{@link #nibbles} holds 4 bits per layer: a uniform layer's value, or a stored layer's slot in {@link #layers}.
 * {@link #stored} marks the stored layers. Slots are numbered in the order layers were materialized, so a new one
 * just goes at the end; {@link #compact()} turns stored layers that became uniform back into a value.
 *
 * <p>Within a layer, cells are {@code x << 4 | z}, two per byte with even {@code z} in the low nibble. That's the
 * flat format's order, {@code x << 8 | y << 4 | z}, within each layer, so {@link #copyTo} and {@link #setAll} move
 * a layer as 16 aligned 8-byte words, one per {@code x}.
 */
final class LayeredNibbles {
    /** one 8-byte run: the 16 cells of an {@code x} row within a layer */
    private static final VarHandle WORD = MethodHandles.byteArrayViewVarHandle(long[].class, ByteOrder.LITTLE_ENDIAN);
    /** times a byte, repeats it in every byte of a word */
    private static final long BYTES_OF = 0x0101_0101_0101_0101L;
    private static final int LAYER_BYTES = 128;
    /** the flat format's length: 4096 nibbles */
    static final int BYTES = 2048;
    private static final byte[] NO_LAYERS = new byte[0];

    /** per layer {@code y}, at bit {@code y << 2}: the value of a uniform layer, the slot of a stored one */
    private long nibbles;
    /** bit {@code y}: layer {@code y} is stored in full */
    private int stored;
    /** the stored layers, {@link #LAYER_BYTES} each, by slot */
    private byte[] layers = NO_LAYERS;

    /**
     * {@return the value of the cell}
     * Kept small: the light engine inlines it into callers that are already large, and every byte of it there can
     * cost another call its inlining.
     */
    int get(int x, int y, int z) {
        int nibble = nibble(y);
        return (stored & (1 << y)) == 0 ? nibble : getStored(nibble, x, z);
    }

    private int getStored(int slot, int x, int z) {
        return (layers[slot << 7 | x << 3 | z >> 1] >> ((z & 1) << 2)) & 15;
    }

    void set(int x, int y, int z, int value) {
        value &= 15;
        int slot = nibble(y);
        if ((stored & (1 << y)) == 0) {
            if (slot == value) return;
            slot = materialize(y, slot);
        }
        int i = slot << 7 | x << 3 | z >> 1;
        int shift = (z & 1) << 2;
        layers[i] = (byte) ((layers[i] & ~(15 << shift)) | (value << shift));
    }

    private int nibble(int y) {
        return (int) (nibbles >>> (y << 2)) & 15;
    }

    private static long withNibble(long nibbles, int y, int nibble) {
        int shift = y << 2;
        return (nibbles & ~(15L << shift)) | ((long) nibble << shift);
    }

    /**
     * {@return whether every cell holds {@code value}}
     * Exact after {@link #compact()}; before it, a stored layer whose cells became uniform still counts as different.
     */
    boolean isUniform(int value) {
        return stored == 0 && nibbles == (value & 15) * 0x1111_1111_1111_1111L;
    }

    /** sets every cell to {@code value} */
    void fill(int value) {
        nibbles = (value & 15) * 0x1111_1111_1111_1111L;
        stored = 0;
        layers = NO_LAYERS;
    }

    /**
     * Stores layer {@code y} in full, every cell holding {@code value}, in the next free slot.
     *
     * @return the slot
     */
    private int materialize(int y, int value) {
        int slot = Integer.bitCount(stored);
        int end = (slot + 1) * LAYER_BYTES;
        if (layers.length < end) layers = Arrays.copyOf(layers, Math.min(16 * LAYER_BYTES, Math.max(end, layers.length * 2)));
        Arrays.fill(layers, slot * LAYER_BYTES, end, (byte) (value * 0x11));
        nibbles = withNibble(nibbles, y, slot);
        stored |= 1 << y;
        return slot;
    }

    /** turns stored layers whose cells all hold one value back into that value, and renumbers the rest */
    void compact() {
        if (stored != 0) pack(layers, false);
    }

    /** replaces every cell from the flat format, as {@link #copyTo} writes it */
    void setAll(byte[] data) {
        pack(data, true);
    }

    /**
     * Rebuilds the layers from {@code source}: the ones whose cells all hold one value become that value, the rest
     * are stored, in a single allocation of their exact size.
     *
     * @param flat whether {@code source} is in the flat format; if not, it's {@link #layers}, and only the stored
     *             layers are read from it
     */
    private void pack(byte[] source, boolean flat) {
        int candidates = flat ? 0xFFFF : stored;
        int stride = flat ? 128 : 8;
        long nibbles = this.nibbles;
        int stored = 0;
        for (int y = 0; y < 16; y++) {
            if ((candidates & (1 << y)) == 0) continue;
            int value = uniformValue(source, flat ? y << 3 : nibble(y) << 7, stride);
            if (value >= 0) nibbles = withNibble(nibbles, y, value);
            else stored |= 1 << y;
        }
        // nothing became uniform, as in most saves
        if (!flat && stored == candidates) return;
        byte[] layers = stored == 0 ? NO_LAYERS : new byte[Integer.bitCount(stored) * LAYER_BYTES];
        int slot = 0;
        for (int y = 0; y < 16; y++) {
            if ((stored & (1 << y)) == 0) continue;
            int from = flat ? y << 3 : nibble(y) << 7;
            for (int x = 0; x < 16; x++) WORD.set(layers, slot << 7 | x << 3, (long) WORD.get(source, from + x * stride));
            nibbles = withNibble(nibbles, y, slot++);
        }
        this.nibbles = nibbles;
        this.stored = stored;
        this.layers = layers;
    }

    /**
     * {@return the value every cell of a layer holds, or -1 if they differ}
     *
     * @param from the layer's first word, its {@code x == 0} row
     * @param stride bytes from one row's word to the next: 8 within {@link #layers}, 128 in the flat format
     */
    private static int uniformValue(byte[] array, int from, int stride) {
        long first = (long) WORD.get(array, from);
        int value = (int) first & 15;
        if (first != value * 0x11 * BYTES_OF) return -1;
        for (int x = 1; x < 16; x++) {
            if ((long) WORD.get(array, from + x * stride) != first) return -1;
        }
        return value;
    }

    /**
     * Writes every cell in the flat format: {@link #BYTES} bytes, cell {@code x << 8 | y << 4 | z}, even cells in the
     * low nibble.
     */
    void copyTo(byte[] out) {
        for (int y = 0; y < 16; y++) {
            int nibble = nibble(y);
            if ((stored & (1 << y)) == 0) {
                long word = nibble * 0x11 * BYTES_OF;
                for (int x = 0; x < 16; x++) WORD.set(out, x << 7 | y << 3, word);
            } else {
                for (int x = 0; x < 16; x++) WORD.set(out, x << 7 | y << 3, (long) WORD.get(layers, nibble << 7 | x << 3));
            }
        }
    }
}
