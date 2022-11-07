package net.modificationstation.stationapi.api.world.chunk.light;

import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;

public interface LightingView {

    void checkBlock(BlockPos var1);

    void addLightSource(BlockPos var1, int var2);

    boolean hasUpdates();

    int doLightUpdates(int var1, boolean var2, boolean var3);

    default void setSectionStatus(BlockPos pos, boolean notReady) {
        this.setSectionStatus(ChunkSectionPos.from(pos), notReady);
    }

    void setSectionStatus(ChunkSectionPos var1, boolean var2);

    void setColumnEnabled(ChunkPos var1, boolean var2);
}

