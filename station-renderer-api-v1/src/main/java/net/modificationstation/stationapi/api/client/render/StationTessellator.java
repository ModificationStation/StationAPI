package net.modificationstation.stationapi.api.client.render;

import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.Util;

public interface StationTessellator {

    default void quad(BakedQuad quad, float x, float y, float z, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        Util.assertImpl();
    }

    default void ensureBufferCapacity(int criticalCapacity) {
        Util.assertImpl();
    }
}
