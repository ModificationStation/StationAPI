package net.modificationstation.stationapi.api.util.collection;

import net.modificationstation.stationapi.api.world.chunk.CompactingPackedIntegerArray;
import net.modificationstation.stationapi.impl.world.chunk.Palette;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntConsumer;

public class PackedIntegerArray
implements PaletteStorage, CompactingPackedIntegerArray {
    /**
     * Magic constants for faster integer division by a constant.
     * 
     * <p>This is computed as {@code (n * scale + offset) >> (32 + shift)}. For a divisor n,
     * the constants are stored as such:
     * 
     * <ul>
     * <li>scale at 3 * (n - 1)</li>
     * <li>offset at 3 * (n - 1) + 1</li>
     * <li>shift at 3 * (n - 1) + 2</li>
     * </ul>
     */
    private static final int[] INDEX_PARAMETERS = new int[]{-1, -1, 0, Integer.MIN_VALUE, 0, 0, 0x55555555, 0x55555555, 0, Integer.MIN_VALUE, 0, 1, 0x33333333, 0x33333333, 0, 0x2AAAAAAA, 0x2AAAAAAA, 0, 0x24924924, 0x24924924, 0, Integer.MIN_VALUE, 0, 2, 0x1C71C71C, 0x1C71C71C, 0, 0x19999999, 0x19999999, 0, 390451572, 390451572, 0, 0x15555555, 0x15555555, 0, 0x13B13B13, 0x13B13B13, 0, 306783378, 306783378, 0, 0x11111111, 0x11111111, 0, Integer.MIN_VALUE, 0, 3, 0xF0F0F0F, 0xF0F0F0F, 0, 0xE38E38E, 0xE38E38E, 0, 226050910, 226050910, 0, 0xCCCCCCC, 0xCCCCCCC, 0, 0xC30C30C, 0xC30C30C, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 0xAAAAAAA, 0xAAAAAAA, 0, 171798691, 171798691, 0, 0x9D89D89, 0x9D89D89, 0, 159072862, 159072862, 0, 0x9249249, 0x9249249, 0, 148102320, 148102320, 0, 0x8888888, 0x8888888, 0, 138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 0x7878787, 0x7878787, 0, 0x7507507, 0x7507507, 0, 0x71C71C7, 0x71C71C7, 0, 116080197, 116080197, 0, 113025455, 113025455, 0, 0x6906906, 0x6906906, 0, 0x6666666, 0x6666666, 0, 104755299, 104755299, 0, 0x6186186, 0x6186186, 0, 99882960, 99882960, 0, 97612893, 97612893, 0, 0x5B05B05, 0x5B05B05, 0, 93368854, 93368854, 0, 91382282, 91382282, 0, 0x5555555, 0x5555555, 0, 87652393, 87652393, 0, 85899345, 85899345, 0, 0x5050505, 0x5050505, 0, 0x4EC4EC4, 0x4EC4EC4, 0, 81037118, 81037118, 0, 79536431, 79536431, 0, 78090314, 78090314, 0, 0x4924924, 0x4924924, 0, 75350303, 75350303, 0, 74051160, 74051160, 0, 72796055, 72796055, 0, 0x4444444, 0x4444444, 0, 70409299, 70409299, 0, 69273666, 69273666, 0, 0x4104104, 0x4104104, 0, Integer.MIN_VALUE, 0, 5};
    private final long[] data;
    private final int elementBits;
    private final long maxValue;
    private final int size;
    private final int elementsPerLong;
    private final int indexScale;
    private final int indexOffset;
    private final int indexShift;

    public PackedIntegerArray(int i, int j, int[] is) {
        this(i, j);
        int l;
        int k = 0;
        for (l = 0; l <= j - this.elementsPerLong; l += this.elementsPerLong) {
            long m = 0L;
            for (int n = this.elementsPerLong - 1; n >= 0; --n) {
                m <<= i;
                m |= (long)is[l + n] & this.maxValue;
            }
            this.data[k++] = m;
        }
        int m = j - l;
        if (m > 0) {
            long o = 0L;
            for (int p = m - 1; p >= 0; --p) {
                o <<= i;
                o |= (long)is[l + p] & this.maxValue;
            }
            this.data[k] = o;
        }
    }

    public PackedIntegerArray(int elementBits, int size) {
        this(elementBits, size, (long[])null);
    }

    public PackedIntegerArray(int elementBits, int size, @SuppressWarnings("NullableProblems") @Nullable long[] data) {
        Validate.inclusiveBetween(1L, 32L, elementBits);
        this.size = size;
        this.elementBits = elementBits;
        this.maxValue = (1L << elementBits) - 1L;
        this.elementsPerLong = (char)(64 / elementBits);
        int i = 3 * (this.elementsPerLong - 1);
        this.indexScale = INDEX_PARAMETERS[i];
        this.indexOffset = INDEX_PARAMETERS[i + 1];
        this.indexShift = INDEX_PARAMETERS[i + 2];
        int j = (size + this.elementsPerLong - 1) / this.elementsPerLong;
        if (data != null) {
            if (data.length != j) {
                throw new InvalidLengthException("Invalid length given for storage, got: " + data.length + " but expected: " + j);
            }
            this.data = data;
        } else {
            this.data = new long[j];
        }
    }

    private int getStorageIndex(int index) {
        long l = Integer.toUnsignedLong(this.indexScale);
        long m = Integer.toUnsignedLong(this.indexOffset);
        return (int)((long)index * l + m >> 32 >> this.indexShift);
    }

    @Override
    public int swap(int index, int value) {
        Validate.inclusiveBetween(0L, this.size - 1, index);
        Validate.inclusiveBetween(0L, this.maxValue, value);
        int i = this.getStorageIndex(index);
        long l = this.data[i];
        int j = (index - i * this.elementsPerLong) * this.elementBits;
        int k = (int)(l >> j & this.maxValue);
        this.data[i] = l & (~(this.maxValue << j)) | ((long)value & this.maxValue) << j;
        return k;
    }

    @Override
    public void set(int index, int value) {
        Validate.inclusiveBetween(0L, this.size - 1, index);
        Validate.inclusiveBetween(0L, this.maxValue, value);
        int i = this.getStorageIndex(index);
        long l = this.data[i];
        int j = (index - i * this.elementsPerLong) * this.elementBits;
        this.data[i] = l & (~(this.maxValue << j)) | ((long)value & this.maxValue) << j;
    }

    @Override
    public int get(int index) {
        Validate.inclusiveBetween(0L, this.size - 1, index);
        int i = this.getStorageIndex(index);
        long l = this.data[i];
        int j = (index - i * this.elementsPerLong) * this.elementBits;
        return (int)(l >> j & this.maxValue);
    }

    @Override
    public long[] getData() {
        return this.data;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public int getElementBits() {
        return this.elementBits;
    }

    @Override
    public void forEach(IntConsumer action) {
        int i = 0;
        for (long l : this.data) {
            for (int j = 0; j < this.elementsPerLong; ++j) {
                action.accept((int)(l & this.maxValue));
                l >>= this.elementBits;
                if (++i < this.size) continue;
                return;
            }
        }
    }

    @Override
    public void applyBrightness92(int[] is) {
        int m;
        long l;
        int k;
        int i = this.data.length;
        int j = 0;
        for (k = 0; k < i - 1; ++k) {
            l = this.data[k];
            for (m = 0; m < this.elementsPerLong; ++m) {
                is[j + m] = (int)(l & this.maxValue);
                l >>= this.elementBits;
            }
            j += this.elementsPerLong;
        }
        k = this.size - j;
        if (k > 0) {
            l = this.data[i - 1];
            for (m = 0; m < k; ++m) {
                is[j + m] = (int)(l & this.maxValue);
                l >>= this.elementBits;
            }
        }
    }

    @Override
    public <T> void compact(Palette<T> srcPalette, Palette<T> dstPalette, short[] out) {
        if (this.size >= Short.MAX_VALUE) {
            throw new IllegalStateException("Array too large");
        }

        if (this.size != out.length) {
            throw new IllegalStateException("Array size mismatch");
        }

        short[] mappings = new short[(int) (this.maxValue + 1)];

        int idx = 0;

        for (long word : this.data) {
            long bits = word;

            for (int elementIdx = 0; elementIdx < this.elementsPerLong; ++elementIdx) {
                int value = (int) (bits & this.maxValue);
                int remappedId = mappings[value];

                if (remappedId == 0) {
                    remappedId = dstPalette.index(srcPalette.get(value)) + 1;
                    mappings[value] = (short) remappedId;
                }

                out[idx] = (short) (remappedId - 1);
                bits >>= this.elementBits;

                ++idx;

                if (idx >= this.size) {
                    return;
                }
            }
        }
    }

    @Override
    public PaletteStorage copy() {
        return new PackedIntegerArray(this.elementBits, this.size, this.data.clone());
    }

    public static class InvalidLengthException
    extends RuntimeException {
        InvalidLengthException(String message) {
            super(message);
        }
    }
}

