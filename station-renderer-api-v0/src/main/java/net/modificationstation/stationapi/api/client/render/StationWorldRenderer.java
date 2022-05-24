package net.modificationstation.stationapi.api.client.render;

import net.minecraft.client.render.WorldRenderer;
import net.modificationstation.stationapi.impl.client.render.StationWorldRendererImpl;

public interface StationWorldRenderer {

    VboPool getTerrainVboPool();

    static StationWorldRenderer get(WorldRenderer worldRenderer) {
        return StationWorldRendererImpl.get(worldRenderer);
    }
}
