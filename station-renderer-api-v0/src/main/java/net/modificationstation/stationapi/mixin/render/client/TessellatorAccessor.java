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
    boolean getDisableColour();

    @Accessor
    void setHasColour(boolean hasColour);

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
    int getColour();

    @Accessor
    int getNormal();

    @Accessor("vertexAmount")
    int stationapi$getVertexAmount();

    @Accessor("vertexAmount")
    void stationapi$setVertexAmount(int vertexAmount);

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

    @Accessor("bufferArray")
    void stationapi$setBufferArray(int[] bufferArray);

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
