package net.modificationstation.stationapi.api.util.collection;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntConsumer;

/**
 * An empty palette storage has a size, but all its elements are 0.
 */
public class EmptyPaletteStorage implements PaletteStorage {
    public static final long[] EMPTY_DATA = new long[0];
    private final int size;

    public EmptyPaletteStorage(int size) {
        this.size = size;
    }

    @Override
    public int swap(int index, int value) {
        Objects.checkIndex(index, this.size);

        if (value != 0) {
            throw new IllegalArgumentException("Invalid value, cannot set a non-zero value into an EmptyPaletteStorage: " + value);
        }
        
        return 0;
    }

    @Override
    public void set(int index, int value) {
        Objects.checkIndex(index, this.size);
        
        if (value != 0) {
            throw new IllegalArgumentException("Invalid value, cannot set a non-zero value into an EmptyPaletteStorage: " + value);
        }
    }

    @Override
    public int get(int index) {
        Objects.checkIndex(index, this.size);
        return 0;
    }

    @Override
    public long[] getData() {
        return EMPTY_DATA;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public int getElementBits() {
        return 0;
    }

    @Override
    public void forEach(IntConsumer action) {
        for(int i = 0; i < this.size; ++i) {
            action.accept(0);
        }

    }

    @Override
    public void writePaletteIndices(int[] is) {
        Arrays.fill(is, 0, this.size, 0);
    }

    @Override
    public PaletteStorage copy() {
        return this;
    }
}

