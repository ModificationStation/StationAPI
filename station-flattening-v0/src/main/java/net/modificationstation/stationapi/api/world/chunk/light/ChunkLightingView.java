package net.modificationstation.stationapi.api.world.chunk.light;

import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.impl.level.chunk.NibbleArray;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.jetbrains.annotations.Nullable;

public interface ChunkLightingView
extends LightingView {
    @Nullable NibbleArray getLightSection(ChunkSectionPos var1);

    int getLightLevel(BlockPos var1);

    enum Empty implements ChunkLightingView {
        INSTANCE;


        @Override
        @Nullable
        public NibbleArray getLightSection(ChunkSectionPos pos) {
            return null;
        }

        @Override
        public int getLightLevel(BlockPos pos) {
            return 0;
        }

        @Override
        public void checkBlock(BlockPos pos) {}

        @Override
        public void addLightSource(BlockPos pos, int level) {}

        @Override
        public boolean hasUpdates() {
            return false;
        }

        @Override
        public int doLightUpdates(int i, boolean doSkylight, boolean skipEdgeLightPropagation) {
            return i;
        }

        @Override
        public void setSectionStatus(ChunkSectionPos pos, boolean notReady) {}

        @Override
        public void setColumnEnabled(ChunkPos pos, boolean retainData) {}
    }
}

