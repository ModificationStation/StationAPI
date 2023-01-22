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

    public void addVertex(double x, double y, double z) {
        a.stationapi$setVertexAmount(a.stationapi$getVertexAmount() + 1);
        //noinspection ConstantConditions
        if (a.stationapi$getDrawingMode() == 7 && TessellatorAccessor.stationapi$getUseTriangles() && a.stationapi$getVertexAmount() % 4 == 0) {
            for(int var7 = 0; var7 < 2; ++var7) {
                int var8 = 8 * (3 - var7);
                if (a.stationapi$getHasTexture()) {
                    a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 3] = a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() - var8 + 3];
                    a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 4] = a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() - var8 + 4];
                }

                if (a.getHasColour()) {
                    a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 5] = a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() - var8 + 5];
                }

                a.stationapi$getBufferArray()[a.stationapi$getBufferPosition()] = a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() - var8];
                a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 1] = a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() - var8 + 1];
                a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 2] = a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() - var8 + 2];
                a.stationapi$setVertexCount(a.stationapi$getVertexCount() + 1);
                a.stationapi$setBufferPosition(a.stationapi$getBufferPosition() + 8);
            }
        }

        if (a.stationapi$getHasTexture()) {
            a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 3] = Float.floatToRawIntBits((float) a.getTextureX());
            a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 4] = Float.floatToRawIntBits((float) a.getTextureY());
        }

        if (a.getHasColour()) {
            a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 5] = a.getColour();
        }

        if (a.getHasNormals()) {
            a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 6] = a.getNormal();
        }

        a.stationapi$getBufferArray()[a.stationapi$getBufferPosition()] = Float.floatToRawIntBits((float)(x + a.getXOffset()));
        a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 1] = Float.floatToRawIntBits((float)(y + a.getYOffset()));
        a.stationapi$getBufferArray()[a.stationapi$getBufferPosition() + 2] = Float.floatToRawIntBits((float)(z + a.getZOffset()));
        a.stationapi$setBufferPosition(a.stationapi$getBufferPosition() + 8);
        a.stationapi$setVertexCount(a.stationapi$getVertexCount() + 1);
        if (a.stationapi$getVertexCount() % 4 == 0)
            ((StationTessellator) tessellator).ensureBufferCapacity(48);
    }
}
