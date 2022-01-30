package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.*;

@Mixin(Tessellator.class)
public interface TessellatorAccessor {

    @Accessor
    boolean getDisableColour();

    @Accessor
    void setDisableColour(boolean disableColour);

    @Accessor
    boolean getHasColour();

    @Accessor
    void setHasColour(boolean hasColour);

    @Accessor
    boolean getHasNormals();

    @Accessor
    void setHasNormals(boolean hasNormals);

    @Accessor("hasTexture")
    boolean stationapi$getHasTexture();

    @Accessor
    void setHasTexture(boolean hasTexture);

    @Invoker("<init>")
    static Tessellator newInst(int bufferSize) {
        throw new AssertionError("Mixin!");
    }

    @Accessor
    boolean getDrawing();

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
        throw new AssertionError("Mixin!");
    }

    @Accessor("drawingMode")
    int stationapi$getDrawingMode();

    @Accessor("bufferArray")
    int[] stationapi$getBufferArray();

    @Accessor("bufferArray")
    void stationapi$setBufferArray(int[] bufferArray);

    @Accessor("field_2068")
    int stationapi$getBufferPosition();

    @Accessor("field_2068")
    void stationapi$setBufferPosition(int bufferPosition);

    @Accessor("bufferSize")
    int stationapi$getBufferSize();

    @Accessor("bufferSize")
    void stationapi$setBufferSize(int bufferSize);

    @Accessor("vertexCount")
    int stationapi$getVertexCount();

    @Accessor("vertexCount")
    void stationapi$setVertexCount(int vertexCount);

    @Accessor("byteBuffer")
    ByteBuffer stationapi$getByteBuffer();

    @Accessor("byteBuffer")
    void stationapi$setByteBuffer(ByteBuffer byteBuffer);

    @Accessor("intBuffer")
    void stationapi$setIntBuffer(IntBuffer intBuffer);

    @Accessor("floatBuffer")
    void stationapi$setFloatBuffer(FloatBuffer floatBuffer);
}
