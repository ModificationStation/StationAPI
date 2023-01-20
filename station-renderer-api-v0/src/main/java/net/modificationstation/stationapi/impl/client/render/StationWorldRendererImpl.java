package net.modificationstation.stationapi.impl.client.render;

import net.minecraft.client.render.WorldRenderer;
import net.modificationstation.stationapi.api.client.render.StationWorldRenderer;
import net.modificationstation.stationapi.api.client.render.VboPool;
import net.modificationstation.stationapi.api.client.render.VertexFormats;
import net.modificationstation.stationapi.mixin.render.client.WorldRendererAccessor;

public class StationWorldRendererImpl implements StationWorldRenderer {

    private final WorldRendererAccessor _this;
    private VboPool terrainVboPool;

    public StationWorldRendererImpl(WorldRenderer worldRenderer) {
        _this = (WorldRendererAccessor) worldRenderer;
    }

    public void resetVboPool() {
        if (terrainVboPool != null)
            terrainVboPool.deleteGlBuffers();
        terrainVboPool = new VboPool(VertexFormats.POSITION_TEXTURE_COLOR_NORMAL);
    }

    @Override
    public VboPool getTerrainVboPool() {
        return terrainVboPool;
    }

    public static StationWorldRendererImpl get(WorldRenderer worldRenderer) {
        return ((StationWorldRendererAccess) worldRenderer).stationapi$stationWorldRenderer();
    }

    public interface StationWorldRendererAccess {

        StationWorldRendererImpl stationapi$stationWorldRenderer();
    }
}
