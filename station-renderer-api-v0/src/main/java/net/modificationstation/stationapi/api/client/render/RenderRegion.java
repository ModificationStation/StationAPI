package net.modificationstation.stationapi.api.client.render;

import net.minecraft.client.render.RenderList;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.client.gl.GlUniform;
import net.modificationstation.stationapi.api.client.gl.VertexBuffer;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import net.modificationstation.stationapi.mixin.render.client.RenderListAccessor;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class RenderRegion extends RenderList {

    private final RenderListAccessor _super = (RenderListAccessor) this;
    private final List<VertexBuffer> buffers = new ArrayList<>();

    public RenderRegion() {
        _super.stationapi$setField_2486(IntBuffer.allocate(0));
    }

    @Override
    public void method_1912(int i, int j, int k, double d, double e, double f) {
        super.method_1912(i, j, k, d, e, f);
        buffers.clear();
    }

    @Override
    public void method_1910(int i) {
        throw new UnsupportedOperationException("Call lists can't be added to VBO regions!");
    }

    public void addBuffer(VertexBuffer buffer) {
        buffers.add(buffer);
    }

    public void method_1909() {
        if (!_super.stationapi$getField_2487()) {
            return;
        }
        if (!buffers.isEmpty()) {
            Shader shader = RenderSystem.getShader();
            GlUniform chunkOffset = null;
            if (shader != null) {
                chunkOffset = shader.chunkOffset;
            }
            if (chunkOffset != null) {
                chunkOffset.set(_super.stationapi$getField_2480() - _super.stationapi$getField_2483(), _super.stationapi$getField_2481() - _super.stationapi$getField_2484(), _super.stationapi$getField_2482() - _super.stationapi$getField_2485());
                chunkOffset.upload();
            }
            for (int buffer = 0, size = buffers.size(); buffer < size; buffer++) {
                buffers.get(buffer).drawVertices();
            }
            if (chunkOffset != null) {
                chunkOffset.set(Vec3f.ZERO);
            }
        }
    }
}
