package net.modificationstation.stationapi.impl.client.texture.plugin;

import net.minecraft.class_214;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.texture.plugin.TessellatorPlugin;
import net.modificationstation.stationapi.impl.client.texture.FastTessellator;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.*;
import java.util.*;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

final class StationTessellator extends TessellatorPlugin implements FastTessellator {

    private final TessellatorAccessor a;
    private final int[] fastVertexData = new int[32];

    StationTessellator(Tessellator tessellator) {
        super(tessellator);
        a = (TessellatorAccessor) tessellator;
    }

    @Override
    public void addVertex(double x, double y, double z, CallbackInfo ci) {
        addVertex(x, y, z);
        ci.cancel();
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
            ensureBufferCapacity(48);
    }

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

    @Override
    public void quad(int[] vertexData, float x, float y, float z, int[] colour) {
        System.arraycopy(vertexData, 0, fastVertexData, 0, 32);
        fastVertexData[0] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[0]) + x + a.getXOffset()));
        fastVertexData[1] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[1]) + y + a.getYOffset()));
        fastVertexData[2] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[2]) + z + a.getZOffset()));
        fastVertexData[5] = colour[0];
        fastVertexData[8] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[8]) + x + a.getXOffset()));
        fastVertexData[9] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[9]) + y + a.getYOffset()));
        fastVertexData[10] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[10]) + z + a.getZOffset()));
        fastVertexData[13] = colour[1];
        fastVertexData[16] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[16]) + x + a.getXOffset()));
        fastVertexData[17] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[17]) + y + a.getYOffset()));
        fastVertexData[18] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[18]) + z + a.getZOffset()));
        fastVertexData[21] = colour[2];
        fastVertexData[24] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[24]) + x + a.getXOffset()));
        fastVertexData[25] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[25]) + y + a.getYOffset()));
        fastVertexData[26] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[26]) + z + a.getZOffset()));
        fastVertexData[29] = colour[3];
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
}
