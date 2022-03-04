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

import java.nio.*;

public interface StationTessellator extends VertexConsumer {

    ByteBuffer BUFFER = class_214.method_744(32);

    static StationTessellator get(Tessellator tessellator) {
        return StationTessellatorImpl.get(tessellator);
    }

    void quad(int[] vertexData, float x, float y, float z, int colour0, int colour1, int colour2, int colour3);

    void ensureBufferCapacity(int criticalCapacity);

    @Override
    default void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        float[] fs = new float[]{brightnesses[0], brightnesses[1], brightnesses[2], brightnesses[3]};
        int[] is = new int[]{lights[0], lights[1], lights[2], lights[3]};
        int[] js = quad.getVertexData();
        Vec3i vec3i = quad.getFace().vector;
        Vector3f vec3f = new Vector3f(vec3i.x, vec3i.y, vec3i.z);
        Matrix4f matrix4f = matrixEntry.getModel();
        vec3f.transform(matrixEntry.getNormal());
        int j = js.length / 8;
        ByteBuffer byteBuffer = BUFFER;
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        for (int k = 0; k < j; ++k) {
            float q;
            float p;
            float o;
            float n;
            float m;
            intBuffer.clear();
            intBuffer.put(js, k * 8, 8);
            float f = byteBuffer.getFloat(0);
            float g = byteBuffer.getFloat(4);
            float h = byteBuffer.getFloat(8);
            if (useQuadColorData) {
                float l = (float) (byteBuffer.get(20) & 0xFF) / 255.0f;
                m = (float) (byteBuffer.get(21) & 0xFF) / 255.0f;
                n = (float) (byteBuffer.get(22) & 0xFF) / 255.0f;
                o = l * fs[k] * red;
                p = m * fs[k] * green;
                q = n * fs[k] * blue;
            } else {
                o = fs[k] * red;
                p = fs[k] * green;
                q = fs[k] * blue;
            }
            int r = is[k];
            m = byteBuffer.getFloat(12);
            n = byteBuffer.getFloat(16);
            Vector4f vector4f = new Vector4f(f, g, h, 1.0f);
            vector4f.transform(matrix4f);
            this.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), o, p, q, 1.0f, m, n, overlay, r, vec3f.getX(), vec3f.getY(), vec3f.getZ());
        }
    }
}
