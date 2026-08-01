package net.modificationstation.stationapi.api.vanillafix.world.chunk;

import net.modificationstation.stationapi.api.util.Util;

public interface StationChunkCache {
    default void unloadChunk(int chunkX, int chunkZ) {
        Util.assertImpl();
    }
}
