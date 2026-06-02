package net.modificationstation.stationapi.api.util.collection;

import com.google.common.base.Preconditions;

import java.util.Arrays;
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
        Preconditions.checkArgument(index >= 0 && index < this.size);
        Preconditions.checkArgument(value == 0);
        return 0;
    }

    @Override
    public void set(int index, int value) {
        Preconditions.checkArgument(index >= 0 && index < this.size);
        Preconditions.checkArgument(value == 0);
    }

    @Override
    public int get(int index) {
        Preconditions.checkArgument(index >= 0 && index < this.size);
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

