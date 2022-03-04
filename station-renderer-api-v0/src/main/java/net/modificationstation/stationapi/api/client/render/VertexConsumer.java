package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_214;
import net.minecraft.util.maths.Vec3i;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.ColourHelper;
import net.modificationstation.stationapi.api.util.math.Matrix3f;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.api.util.math.Vector3f;
import net.modificationstation.stationapi.api.util.math.Vector4f;

import java.nio.*;

@Environment(value=EnvType.CLIENT)
public interface VertexConsumer {

    ByteBuffer BUFFER = class_214.method_744(VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL.getVertexSize());

    VertexConsumer vertex(double var1, double var3, double var5);

    VertexConsumer colour(int var1, int var2, int var3, int var4);

    VertexConsumer texture(float var1, float var2);

    VertexConsumer overlay(int var1, int var2);

    VertexConsumer light(int var1, int var2);

    VertexConsumer normal(float var1, float var2, float var3);

    void next();

    default void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
        this.vertex(x, y, z);
        this.colour(red, green, blue, alpha);
        this.texture(u, v);
        this.overlay(overlay);
        this.light(light);
        this.normal(normalX, normalY, normalZ);
        this.next();
    }

    void fixedColor(int var1, int var2, int var3, int var4);

    void unfixColor();

    default VertexConsumer colour(float red, float green, float blue, float alpha) {
        return this.colour((int)(red * 255.0f), (int)(green * 255.0f), (int)(blue * 255.0f), (int)(alpha * 255.0f));
    }

    default VertexConsumer colour(int argb) {
        return this.colour(ColourHelper.Argb.getRed(argb), ColourHelper.Argb.getGreen(argb), ColourHelper.Argb.getBlue(argb), ColourHelper.Argb.getAlpha(argb));
    }

    default VertexConsumer light(int uv) {
        return this.light(uv & ( /* LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE */ 0xF0 | 0xFF0F), uv >> 16 & ( /* LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE */ 0xF0 | 0xFF0F));
    }

    default VertexConsumer overlay(int uv) {
        return this.overlay(uv & 0xFFFF, uv >> 16 & 0xFFFF);
    }

    default void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float red, float green, float blue, int light, int overlay) {
        this.quad(matrixEntry, quad, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, red, green, blue, new int[]{light, light, light, light}, overlay, false);
    }

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
                float l = (float) (byteBuffer.get(12) & 0xFF) / 255.0f;
                m = (float) (byteBuffer.get(13) & 0xFF) / 255.0f;
                n = (float) (byteBuffer.get(14) & 0xFF) / 255.0f;
                o = l * fs[k] * red;
                p = m * fs[k] * green;
                q = n * fs[k] * blue;
            } else {
                o = fs[k] * red;
                p = fs[k] * green;
                q = fs[k] * blue;
            }
            int r = is[k];
            m = byteBuffer.getFloat(16);
            n = byteBuffer.getFloat(20);
            Vector4f vector4f = new Vector4f(f, g, h, 1.0f);
            vector4f.transform(matrix4f);
            this.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), o, p, q, 1.0f, m, n, overlay, r, vec3f.getX(), vec3f.getY(), vec3f.getZ());
        }
    }

    default VertexConsumer vertex(Matrix4f matrix, float x, float y, float z) {
        Vector4f vector4f = new Vector4f(x, y, z, 1.0f);
        vector4f.transform(matrix);
        return this.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ());
    }

    default VertexConsumer normal(Matrix3f matrix, float x, float y, float z) {
        Vector3f vec3f = new Vector3f(x, y, z);
        vec3f.transform(matrix);
        return this.normal(vec3f.getX(), vec3f.getY(), vec3f.getZ());
    }
}

