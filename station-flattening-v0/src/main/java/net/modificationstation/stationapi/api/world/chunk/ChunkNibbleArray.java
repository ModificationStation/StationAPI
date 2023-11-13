package net.modificationstation.stationapi.api.world.chunk;

import net.modificationstation.stationapi.impl.world.chunk.NibbleArray;

public final class ChunkNibbleArray extends NibbleArray {

    public ChunkNibbleArray() {
        super(2048);
    }

    public static int getIndex(int i, int x, int y) {
        return x << 8 | y << 4 | i;
    }
}
