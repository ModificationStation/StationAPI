package net.modificationstation.stationapi.impl.client.render;

import net.minecraft.class_214;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

import java.nio.*;
import java.util.*;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class StationTessellatorImpl implements StationTessellator {

    private final Tessellator self;
    private final TessellatorAccessor access;
    private double vx, vy, vz;

    private final int[] fastVertexData = new int[32];

    public StationTessellatorImpl(Tessellator tessellator) {
        self = tessellator;
        access = (TessellatorAccessor) tessellator;
    }

    public static StationTessellatorImpl get(Tessellator tessellator) {
        return ((StationTessellatorAccess) tessellator).stationapi$stationTessellator();
    }

    @Override
    public void quad(int[] vertexData, float x, float y, float z, int colour0, int colour1, int colour2, int colour3) {
        System.arraycopy(vertexData, 0, fastVertexData, 0, 32);
        fastVertexData[0] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[0]) + x + access.getXOffset()));
        fastVertexData[1] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[1]) + y + access.getYOffset()));
        fastVertexData[2] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[2]) + z + access.getZOffset()));
        fastVertexData[5] = colour0;
        fastVertexData[8] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[8]) + x + access.getXOffset()));
        fastVertexData[9] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[9]) + y + access.getYOffset()));
        fastVertexData[10] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[10]) + z + access.getZOffset()));
        fastVertexData[13] = colour1;
        fastVertexData[16] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[16]) + x + access.getXOffset()));
        fastVertexData[17] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[17]) + y + access.getYOffset()));
        fastVertexData[18] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[18]) + z + access.getZOffset()));
        fastVertexData[21] = colour2;
        fastVertexData[24] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[24]) + x + access.getXOffset()));
        fastVertexData[25] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[25]) + y + access.getYOffset()));
        fastVertexData[26] = Float.floatToRawIntBits((float) (Float.intBitsToFloat(fastVertexData[26]) + z + access.getZOffset()));
        fastVertexData[29] = colour3;
        access.setHasColour(true);
        access.setHasTexture(true);
        System.arraycopy(fastVertexData, 0, access.stationapi$getBufferArray(), access.stationapi$getBufferPosition(), 24);
        System.arraycopy(fastVertexData, 0, access.stationapi$getBufferArray(), access.stationapi$getBufferPosition() + 24, 8);
        System.arraycopy(fastVertexData, 16, access.stationapi$getBufferArray(), access.stationapi$getBufferPosition() + 32, 16);
        access.stationapi$setVertexAmount(access.stationapi$getVertexAmount() + 4);
        access.stationapi$setBufferPosition(access.stationapi$getBufferPosition() + 48);
        access.stationapi$setVertexCount(access.stationapi$getVertexCount() + 6);
        ensureBufferCapacity(48);
    }

    @Override
    public void ensureBufferCapacity(int criticalCapacity) {
        if (access.stationapi$getBufferPosition() >= access.stationapi$getBufferSize() - criticalCapacity) {
            LOGGER.info("Tessellator is nearing its maximum capacity. Increasing the buffer size from {} to {}", access.stationapi$getBufferSize(), access.stationapi$getBufferSize() * 2);
            access.stationapi$setBufferSize(access.stationapi$getBufferSize() * 2);
            access.stationapi$setBufferArray(Arrays.copyOf(access.stationapi$getBufferArray(), access.stationapi$getBufferSize()));
            ByteBuffer newBuffer = class_214.method_744(access.stationapi$getBufferSize() * 4);
            access.stationapi$setByteBuffer(newBuffer);
            access.stationapi$setIntBuffer(newBuffer.asIntBuffer());
            access.stationapi$setFloatBuffer(newBuffer.asFloatBuffer());
        }
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        vx = x;
        vy = y;
        vz = z;
        return this;
    }

    @Override
    public VertexConsumer colour(int r, int g, int b, int a) {
        self.colour(r, g, b, a);
        return this;
    }

    @Override
    public VertexConsumer texture(float u, float v) {
        self.setTextureXY(u, v);
        return this;
    }

    @Override
    public VertexConsumer overlay(int var1, int var2) {
        return this;
    }

    @Override
    public VertexConsumer light(int var1, int var2) {
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        self.setNormal(x, y, z);
        return this;
    }

    @Override
    public void next() {
        self.addVertex(vx, vy, vz);
    }

    @Override
    public void fixedColor(int var1, int var2, int var3, int var4) {
        throw new UnsupportedOperationException("Fixed colour isn't supported in the default tessellator implementation!");
    }

    @Override
    public void unfixColor() {}

    public interface StationTessellatorAccess {

        StationTessellatorImpl stationapi$stationTessellator();
    }
}
