package net.modificationstation.stationapi.api.world.chunk.light;

import net.minecraft.level.LightType;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.world.HeightLimitView;
import net.modificationstation.stationapi.impl.level.chunk.NibbleArray;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;
import net.modificationstation.stationapi.impl.util.math.ChunkSectionPos;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LightingProvider
implements LightingView {
    public static final int field_31713 = 15;
    public static final int field_31714 = 1;
    protected final HeightLimitView world;
    @Nullable
    private final ChunkLightProvider<?, ?> blockLightProvider;
    @Nullable
    private final ChunkLightProvider<?, ?> skyLightProvider;

    public LightingProvider(ChunkProvider chunkProvider, boolean hasBlockLight, boolean hasSkyLight) {
        this.world = chunkProvider.getWorld();
        this.blockLightProvider = hasBlockLight ? new ChunkBlockLightProvider(chunkProvider) : null;
        this.skyLightProvider = hasSkyLight ? new ChunkSkyLightProvider(chunkProvider) : null;
    }

    @Override
    public void checkBlock(BlockPos pos) {
        if (this.blockLightProvider != null) {
            this.blockLightProvider.checkBlock(pos);
        }
        if (this.skyLightProvider != null) {
            this.skyLightProvider.checkBlock(pos);
        }
    }

    @Override
    public void addLightSource(BlockPos pos, int level) {
        if (this.blockLightProvider != null) {
            this.blockLightProvider.addLightSource(pos, level);
        }
    }

    @Override
    public boolean hasUpdates() {
        if (this.skyLightProvider != null && this.skyLightProvider.hasUpdates()) {
            return true;
        }
        return this.blockLightProvider != null && this.blockLightProvider.hasUpdates();
    }

    @Override
    public int doLightUpdates(int i, boolean doSkylight, boolean skipEdgeLightPropagation) {
        if (this.blockLightProvider != null && this.skyLightProvider != null) {
            int j = i / 2;
            int k = this.blockLightProvider.doLightUpdates(j, doSkylight, skipEdgeLightPropagation);
            int l = i - j + k;
            int m = this.skyLightProvider.doLightUpdates(l, doSkylight, skipEdgeLightPropagation);
            if (k == 0 && m > 0) {
                return this.blockLightProvider.doLightUpdates(m, doSkylight, skipEdgeLightPropagation);
            }
            return m;
        }
        if (this.blockLightProvider != null) {
            return this.blockLightProvider.doLightUpdates(i, doSkylight, skipEdgeLightPropagation);
        }
        if (this.skyLightProvider != null) {
            return this.skyLightProvider.doLightUpdates(i, doSkylight, skipEdgeLightPropagation);
        }
        return i;
    }

    @Override
    public void setSectionStatus(ChunkSectionPos pos, boolean notReady) {
        if (this.blockLightProvider != null) {
            this.blockLightProvider.setSectionStatus(pos, notReady);
        }
        if (this.skyLightProvider != null) {
            this.skyLightProvider.setSectionStatus(pos, notReady);
        }
    }

    @Override
    public void setColumnEnabled(ChunkPos pos, boolean retainData) {
        if (this.blockLightProvider != null) {
            this.blockLightProvider.setColumnEnabled(pos, retainData);
        }
        if (this.skyLightProvider != null) {
            this.skyLightProvider.setColumnEnabled(pos, retainData);
        }
    }

    public ChunkLightingView get(LightType lightType) {
        return lightType == LightType.field_2758 ? Objects.requireNonNullElse(this.blockLightProvider, ChunkLightingView.Empty.INSTANCE) : Objects.requireNonNullElse(this.skyLightProvider, ChunkLightingView.Empty.INSTANCE);
    }

    public String displaySectionLevel(LightType lightType, ChunkSectionPos pos) {
        if (lightType == LightType.field_2758) {
            if (this.blockLightProvider != null) {
                return this.blockLightProvider.displaySectionLevel(pos.asLong());
            }
        } else if (this.skyLightProvider != null) {
            return this.skyLightProvider.displaySectionLevel(pos.asLong());
        }
        return "n/a";
    }

    public void enqueueSectionData(LightType lightType, ChunkSectionPos pos, @Nullable NibbleArray nibbles, boolean nonEdge) {
        if (lightType == LightType.field_2758) {
            if (this.blockLightProvider != null) {
                this.blockLightProvider.enqueueSectionData(pos.asLong(), nibbles, nonEdge);
            }
        } else if (this.skyLightProvider != null) {
            this.skyLightProvider.enqueueSectionData(pos.asLong(), nibbles, nonEdge);
        }
    }

    public void setRetainData(ChunkPos pos, boolean retainData) {
        if (this.blockLightProvider != null) {
            this.blockLightProvider.setRetainColumn(pos, retainData);
        }
        if (this.skyLightProvider != null) {
            this.skyLightProvider.setRetainColumn(pos, retainData);
        }
    }

    public int getLight(BlockPos pos, int ambientDarkness) {
        int i = this.skyLightProvider == null ? 0 : this.skyLightProvider.getLightLevel(pos) - ambientDarkness;
        int j = this.blockLightProvider == null ? 0 : this.blockLightProvider.getLightLevel(pos);
        return Math.max(j, i);
    }

    public int getHeight() {
        return this.world.countVerticalSections() + 2;
    }

    public int getBottomY() {
        return this.world.getBottomSectionCoord() - 1;
    }

    public int getTopY() {
        return this.getBottomY() + this.getHeight();
    }
}

