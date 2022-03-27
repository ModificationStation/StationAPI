package net.modificationstation.stationapi.api.client.render;

import net.minecraft.class_214;
import net.minecraft.client.render.Tessellator;
import net.minecraft.util.maths.Vec3i;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.api.util.math.Vector3f;
import net.modificationstation.stationapi.api.util.math.Vector4f;
import net.modificationstation.stationapi.impl.client.render.StationTessellatorImpl;
import org.jetbrains.annotations.ApiStatus;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface StationTessellator extends VertexConsumer {

    @ApiStatus.Internal
    ByteBuffer buffer = class_214.method_744(32);
    @ApiStatus.Internal
    IntBuffer bufferAsInt = buffer.asIntBuffer();
    @ApiStatus.Internal
    Vector4f pos = new Vector4f();
    @ApiStatus.Internal
    Vector3f normal = new Vector3f();

    static StationTessellator get(Tessellator tessellator) {
        return StationTessellatorImpl.get(tessellator);
    }

    void ensureBufferCapacity(int criticalCapacity);

    @Override
    default void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        int[] js = quad.getVertexData();
        Vec3i vec3i = quad.getFace().vector;
        normal.set(vec3i.x, vec3i.y, vec3i.z);
        Matrix4f matrix4f = matrixEntry.getPositionMatrix();
        normal.transform(matrixEntry.getNormalMatrix());
        int j = js.length / 8;
        ByteBuffer byteBuffer = buffer;
        for (int k = 0; k < j; ++k) {
            float q;
            float p;
            float o;
            float n;
            float m;
            bufferAsInt.clear();
            bufferAsInt.put(js, k * 8, 8);
            float f = byteBuffer.getFloat(0);
            float g = byteBuffer.getFloat(4);
            float h = byteBuffer.getFloat(8);
            if (useQuadColorData) {
                float l = (float) (byteBuffer.get(12) & 0xFF) / 255.0f;
                m = (float) (byteBuffer.get(13) & 0xFF) / 255.0f;
                n = (float) (byteBuffer.get(14) & 0xFF) / 255.0f;
                o = l * brightnesses[k] * red;
                p = m * brightnesses[k] * green;
                q = n * brightnesses[k] * blue;
            } else {
                o = brightnesses[k] * red;
                p = brightnesses[k] * green;
                q = brightnesses[k] * blue;
            }
            int r = lights[k];
            m = byteBuffer.getFloat(16);
            n = byteBuffer.getFloat(20);
            pos.set(f, g, h, 1);
            pos.transform(matrix4f);
            this.vertex(pos.getX(), pos.getY(), pos.getZ(), o, p, q, 1.0f, m, n, overlay, r, normal.getX(), normal.getY(), normal.getZ());
        }
    }
}
