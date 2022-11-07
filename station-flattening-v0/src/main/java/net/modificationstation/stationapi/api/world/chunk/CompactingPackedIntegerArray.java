package net.modificationstation.stationapi.api.world.chunk;

import net.modificationstation.stationapi.impl.level.chunk.Palette;

public interface CompactingPackedIntegerArray {

    /**
     * Copies the data out of this array into a new non-packed array. The returned array contains a copy of this array
     * re-mapped using {@param destPalette}.
     */
    <T> void compact(Palette<T> srcPalette, Palette<T> dstPalette, short[] out);
}
