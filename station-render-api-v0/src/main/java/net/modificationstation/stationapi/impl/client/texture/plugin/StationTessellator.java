package net.modificationstation.stationapi.impl.client.texture.plugin;

import net.minecraft.class_214;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.texture.plugin.TessellatorPlugin;
import net.modificationstation.stationapi.impl.client.texture.FastTessellator;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.*;
import java.util.*;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderAPI.LOGGER;

final class StationTessellator extends TessellatorPlugin implements FastTessellator {

    private final TessellatorAccessor tessellatorAccessor;
    private final int[] fastVertexData = new int[32];

    StationTessellator(Tessellator tessellator) {
        super(tessellator);
        tessellatorAccessor = (TessellatorAccessor) tessellator;
    }

    @Override
    public void addVertex(double x, double y, double z, CallbackInfo ci) {
        addVertex(x, y, z);
        ci.cancel();
    }

    public void addVertex(double x, double y, double z) {
        tessellatorAccessor.stationapi$setVertexAmount(tessellatorAccessor.stationapi$getVertexAmount() + 1);
        //noinspection ConstantConditions
        if (tessellatorAccessor.stationapi$getDrawingMode() == 7 && TessellatorAccessor.stationapi$getUseTriangles() && tessellatorAccessor.stationapi$getVertexAmount() % 4 == 0) {
            for(int var7 = 0; var7 < 2; ++var7) {
                int var8 = 8 * (3 - var7);
                if (tessellatorAccessor.stationapi$getHasTexture()) {
                    tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 3] = tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() - var8 + 3];
                    tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 4] = tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() - var8 + 4];
                }

                if (tessellatorAccessor.getHasColour()) {
                    tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 5] = tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() - var8 + 5];
                }

                tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition()] = tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() - var8];
                tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 1] = tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() - var8 + 1];
                tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 2] = tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() - var8 + 2];
                tessellatorAccessor.stationapi$setVertexCount(tessellatorAccessor.stationapi$getVertexCount() + 1);
                tessellatorAccessor.stationapi$setBufferPosition(tessellatorAccessor.stationapi$getBufferPosition() + 8);
            }
        }

        if (tessellatorAccessor.stationapi$getHasTexture()) {
            tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 3] = Float.floatToRawIntBits((float)tessellatorAccessor.getTextureX());
            tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 4] = Float.floatToRawIntBits((float)tessellatorAccessor.getTextureY());
        }

        if (tessellatorAccessor.getHasColour()) {
            tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 5] = tessellatorAccessor.getColour();
        }

        if (tessellatorAccessor.getHasNormals()) {
            tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 6] = tessellatorAccessor.getNormal();
        }

        tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition()] = Float.floatToRawIntBits((float)(x + tessellatorAccessor.getXOffset()));
        tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 1] = Float.floatToRawIntBits((float)(y + tessellatorAccessor.getYOffset()));
        tessellatorAccessor.stationapi$getBufferArray()[tessellatorAccessor.stationapi$getBufferPosition() + 2] = Float.floatToRawIntBits((float)(z + tessellatorAccessor.getZOffset()));
        tessellatorAccessor.stationapi$setBufferPosition(tessellatorAccessor.stationapi$getBufferPosition() + 8);
        tessellatorAccessor.stationapi$setVertexCount(tessellatorAccessor.stationapi$getVertexCount() + 1);
        if (tessellatorAccessor.stationapi$getVertexCount() % 4 == 0)
            ensureBufferCapacity(48);
    }

    public void ensureBufferCapacity(int criticalCapacity) {
        if (tessellatorAccessor.stationapi$getBufferPosition() >= tessellatorAccessor.stationapi$getBufferSize() - criticalCapacity) {
            LOGGER.info("Tessellator is nearing its maximum capacity. Increasing the buffer size from {} to {}", tessellatorAccessor.stationapi$getBufferSize(), tessellatorAccessor.stationapi$getBufferSize() * 2);
            tessellatorAccessor.stationapi$setBufferSize(tessellatorAccessor.stationapi$getBufferSize() * 2);
            tessellatorAccessor.stationapi$setBufferArray(Arrays.copyOf(tessellatorAccessor.stationapi$getBufferArray(), tessellatorAccessor.stationapi$getBufferSize()));
            ByteBuffer newBuffer = class_214.method_744(tessellatorAccessor.stationapi$getBufferSize() * 4);
            tessellatorAccessor.stationapi$setByteBuffer(newBuffer);
            tessellatorAccessor.stationapi$setIntBuffer(newBuffer.asIntBuffer());
            tessellatorAccessor.stationapi$setFloatBuffer(newBuffer.asFloatBuffer());
        }
    }

    @Override
    public void quad(int[] vertexData, float x, float y, float z, int[] colour) {
        System.arraycopy(vertexData, 0, fastVertexData, 0, 32);
        fastVertexData[0] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[0]) + x + tessellatorAccessor.getXOffset()));
        fastVertexData[1] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[1]) + y + tessellatorAccessor.getYOffset()));
        fastVertexData[2] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[2]) + z + tessellatorAccessor.getZOffset()));
        fastVertexData[5] = colour[0];
        fastVertexData[8] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[8]) + x + tessellatorAccessor.getXOffset()));
        fastVertexData[9] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[9]) + y + tessellatorAccessor.getYOffset()));
        fastVertexData[10] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[10]) + z + tessellatorAccessor.getZOffset()));
        fastVertexData[13] = colour[1];
        fastVertexData[16] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[16]) + x + tessellatorAccessor.getXOffset()));
        fastVertexData[17] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[17]) + y + tessellatorAccessor.getYOffset()));
        fastVertexData[18] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[18]) + z + tessellatorAccessor.getZOffset()));
        fastVertexData[21] = colour[2];
        fastVertexData[24] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[24]) + x + tessellatorAccessor.getXOffset()));
        fastVertexData[25] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[25]) + y + tessellatorAccessor.getYOffset()));
        fastVertexData[26] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[26]) + z + tessellatorAccessor.getZOffset()));
        fastVertexData[29] = colour[3];
        tessellatorAccessor.setHasColour(true);
        tessellatorAccessor.setHasTexture(true);
        System.arraycopy(fastVertexData, 0, tessellatorAccessor.stationapi$getBufferArray(), tessellatorAccessor.stationapi$getBufferPosition(), 24);
        System.arraycopy(fastVertexData, 0, tessellatorAccessor.stationapi$getBufferArray(), tessellatorAccessor.stationapi$getBufferPosition() + 24, 8);
        System.arraycopy(fastVertexData, 16, tessellatorAccessor.stationapi$getBufferArray(), tessellatorAccessor.stationapi$getBufferPosition() + 32, 16);
        tessellatorAccessor.stationapi$setVertexAmount(tessellatorAccessor.stationapi$getVertexAmount() + 4);
        tessellatorAccessor.stationapi$setBufferPosition(tessellatorAccessor.stationapi$getBufferPosition() + 48);
        tessellatorAccessor.stationapi$setVertexCount(tessellatorAccessor.stationapi$getVertexCount() + 6);
        ensureBufferCapacity(48);
    }
}
