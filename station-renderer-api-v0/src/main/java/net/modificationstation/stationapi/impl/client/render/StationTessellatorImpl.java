package net.modificationstation.stationapi.impl.client.render;

import net.minecraft.class_214;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

import java.nio.*;
import java.util.*;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class StationTessellatorImpl implements StationTessellator {

    private final TessellatorAccessor a;

    private final int[] fastVertexData = new int[32];

    public StationTessellatorImpl(Tessellator tessellator) {
        a = (TessellatorAccessor) tessellator;
    }

    public static StationTessellatorImpl get(Tessellator tessellator) {
        return ((StationTessellatorAccess) tessellator).stationapi$stationTessellator();
    }

    @Override
    public void quad(int[] vertexData, float x, float y, float z, int colour0, int colour1, int colour2, int colour3) {
        System.arraycopy(vertexData, 0, fastVertexData, 0, 32);
        fastVertexData[0] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[0]) + x + a.getXOffset()));
        fastVertexData[1] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[1]) + y + a.getYOffset()));
        fastVertexData[2] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[2]) + z + a.getZOffset()));
        fastVertexData[5] = colour0;
        fastVertexData[8] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[8]) + x + a.getXOffset()));
        fastVertexData[9] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[9]) + y + a.getYOffset()));
        fastVertexData[10] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[10]) + z + a.getZOffset()));
        fastVertexData[13] = colour1;
        fastVertexData[16] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[16]) + x + a.getXOffset()));
        fastVertexData[17] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[17]) + y + a.getYOffset()));
        fastVertexData[18] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[18]) + z + a.getZOffset()));
        fastVertexData[21] = colour2;
        fastVertexData[24] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[24]) + x + a.getXOffset()));
        fastVertexData[25] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[25]) + y + a.getYOffset()));
        fastVertexData[26] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[26]) + z + a.getZOffset()));
        fastVertexData[29] = colour3;
        a.setHasColour(true);
        a.setHasTexture(true);
        System.arraycopy(fastVertexData, 0, a.stationapi$getBufferArray(), a.stationapi$getBufferPosition(), 24);
        System.arraycopy(fastVertexData, 0, a.stationapi$getBufferArray(), a.stationapi$getBufferPosition() + 24, 8);
        System.arraycopy(fastVertexData, 16, a.stationapi$getBufferArray(), a.stationapi$getBufferPosition() + 32, 16);
        a.stationapi$setVertexAmount(a.stationapi$getVertexAmount() + 4);
        a.stationapi$setBufferPosition(a.stationapi$getBufferPosition() + 48);
        a.stationapi$setVertexCount(a.stationapi$getVertexCount() + 6);
        ensureBufferCapacity(48);
    }

    @Override
    public void ensureBufferCapacity(int criticalCapacity) {
        if (a.stationapi$getBufferPosition() >= a.stationapi$getBufferSize() - criticalCapacity) {
            LOGGER.info("Tessellator is nearing its maximum capacity. Increasing the buffer size from {} to {}", a.stationapi$getBufferSize(), a.stationapi$getBufferSize() * 2);
            a.stationapi$setBufferSize(a.stationapi$getBufferSize() * 2);
            a.stationapi$setBufferArray(Arrays.copyOf(a.stationapi$getBufferArray(), a.stationapi$getBufferSize()));
            ByteBuffer newBuffer = class_214.method_744(a.stationapi$getBufferSize() * 4);
            a.stationapi$setByteBuffer(newBuffer);
            a.stationapi$setIntBuffer(newBuffer.asIntBuffer());
            a.stationapi$setFloatBuffer(newBuffer.asFloatBuffer());
        }
    }

    public interface StationTessellatorAccess {

        StationTessellatorImpl stationapi$stationTessellator();
    }
}
