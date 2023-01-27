package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.mixin.arsenic.client.TessellatorAccessor;

public final class ArsenicTessellator {

    private final Tessellator tessellator;
    private final TessellatorAccessor a;

    public ArsenicTessellator(Tessellator tessellator) {
        this.tessellator = tessellator;
        a = (TessellatorAccessor) tessellator;
    }

    public void afterVertex() {
        if (a.stationapi$getVertexCount() % 4 == 0)
            ((StationTessellator) tessellator).ensureBufferCapacity(48);
    }
}
