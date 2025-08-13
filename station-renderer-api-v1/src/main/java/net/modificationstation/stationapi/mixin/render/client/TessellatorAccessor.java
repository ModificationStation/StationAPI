package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {
    @Accessor
    boolean getColorDisabled();

    @Accessor
    void setHasColor(boolean hasColor);

    @Accessor
    void setHasTexture(boolean hasTexture);

    @Accessor
    void setHasNormals(boolean hasNormals);

    @Accessor
    double getXOffset();

    @Accessor
    double getYOffset();

    @Accessor
    double getZOffset();

    @Accessor
    int getColor();

    @Accessor
    int getNormal();

    @Accessor("addedVertexCount")
    int stationapi$getAddedVertexCount();

    @Accessor("addedVertexCount")
    void stationapi$setAddedVertexCount(int vertexAmount);

    @Accessor("buffer")
    int[] stationapi$getBuffer();

    @Accessor("bufferPosition")
    int stationapi$getBufferPosition();

    @Accessor("bufferPosition")
    void stationapi$setBufferPosition(int bufferPosition);

    @Accessor("vertexCount")
    int stationapi$getVertexCount();

    @Accessor("vertexCount")
    void stationapi$setVertexCount(int vertexCount);

    @Accessor("buffer")
    void stationapi$setBuffer(int[] buffer);

    @Accessor("bufferSize")
    int stationapi$getBufferSize();

    @Accessor("bufferSize")
    void stationapi$setBufferSize(int bufferSize);

    @Accessor("byteBuffer")
    void stationapi$setByteBuffer(ByteBuffer byteBuffer);

    @Accessor("intBuffer")
    void stationapi$setIntBuffer(IntBuffer intBuffer);

    @Accessor("floatBuffer")
    void stationapi$setFloatBuffer(FloatBuffer floatBuffer);
}
