package net.modificationstation.stationapi.impl.client.render;

import net.minecraft.class_214;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.render.StationTessellator;
import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.mixin.render.client.TessellatorAccessor;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static net.modificationstation.stationapi.impl.client.texture.StationRenderImpl.LOGGER;

public class StationTessellatorImpl implements StationTessellator {

    private final Tessellator self;
    private final TessellatorAccessor access;
    private double vx, vy, vz;

    public StationTessellatorImpl(Tessellator tessellator) {
        self = tessellator;
        access = (TessellatorAccessor) tessellator;
    }

    public static StationTessellatorImpl get(Tessellator tessellator) {
        return ((StationTessellatorAccess) tessellator).stationapi$stationTessellator();
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
    public VertexConsumer color(int r, int g, int b, int a) {
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
