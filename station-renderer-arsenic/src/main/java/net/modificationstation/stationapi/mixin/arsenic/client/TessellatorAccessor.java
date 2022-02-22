package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {

    @Accessor
    boolean getHasColour();

    @Accessor
    boolean getHasNormals();

    @Accessor("hasTexture")
    boolean stationapi$getHasTexture();

    @Accessor
    double getXOffset();

    @Accessor
    double getYOffset();

    @Accessor
    double getZOffset();

    @Accessor
    int getColour();

    @Accessor
    int getNormal();

    @Accessor
    double getTextureX();

    @Accessor
    double getTextureY();

    @Accessor("vertexAmount")
    int stationapi$getVertexAmount();

    @Accessor("vertexAmount")
    void stationapi$setVertexAmount(int vertexAmount);

    @Accessor("useTriangles")
    static boolean stationapi$getUseTriangles() {
        return Util.assertMixin();
    }

    @Accessor("drawingMode")
    int stationapi$getDrawingMode();

    @Accessor("bufferArray")
    int[] stationapi$getBufferArray();

    @Accessor("field_2068")
    int stationapi$getBufferPosition();

    @Accessor("field_2068")
    void stationapi$setBufferPosition(int bufferPosition);

    @Accessor("vertexCount")
    int stationapi$getVertexCount();

    @Accessor("vertexCount")
    void stationapi$setVertexCount(int vertexCount);
}
