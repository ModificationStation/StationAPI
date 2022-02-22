package net.modificationstation.stationapi.api.client.render;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.impl.client.render.StationTessellatorImpl;

public interface StationTessellator {

    static StationTessellator get(Tessellator tessellator) {
        return StationTessellatorImpl.get(tessellator);
    }

    void quad(int[] vertexData, float x, float y, float z, int colour0, int colour1, int colour2, int colour3);

    void ensureBufferCapacity(int criticalCapacity);
}
