package net.modificationstation.stationapi.api.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_214;
import net.minecraft.util.maths.Vec3i;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

@Environment(value=EnvType.CLIENT)
public interface VertexConsumer {

    ByteBuffer BUFFER = class_214.method_744(VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.getVertexSizeByte());

    VertexConsumer vertex(double var1, double var3, double var5);

    VertexConsumer color(int var1, int var2, int var3, int var4);

    VertexConsumer texture(float var1, float var2);

    VertexConsumer overlay(int var1, int var2);

    VertexConsumer light(int var1, int var2);

    VertexConsumer normal(float var1, float var2, float var3);

    void next();

    default void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
        this.vertex(x, y, z);
        this.color(red, green, blue, alpha);
        this.texture(u, v);
        this.overlay(overlay);
        this.light(light);
        this.normal(normalX, normalY, normalZ);
        this.next();
    }

    void fixedColor(int var1, int var2, int var3, int var4);

    void unfixColor();

    default VertexConsumer color(float red, float green, float blue, float alpha) {
        return this.color((int)(red * 255.0f), (int)(green * 255.0f), (int)(blue * 255.0f), (int)(alpha * 255.0f));
    }

    default VertexConsumer color(int argb) {
        return this.color(ColourHelper.Argb.getRed(argb), ColourHelper.Argb.getGreen(argb), ColourHelper.Argb.getBlue(argb), ColourHelper.Argb.getAlpha(argb));
    }

    default VertexConsumer light(int uv) {
        return this.light(uv & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 0xFF0F), uv >> 16 & (LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE | 0xFF0F));
    }

    default VertexConsumer overlay(int uv) {
        return this.overlay(uv & 0xFFFF, uv >> 16 & 0xFFFF);
    }

    default void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float red, float green, float blue, int light, int overlay) {
        this.quad(matrixEntry, quad, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, red, green, blue, new int[]{light, light, light, light}, overlay, false);
    }

    default void quad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] brightnesses, float red, float green, float blue, int[] lights, int overlay, boolean useQuadColorData) {
        int[] js = quad.getVertexData();
        Vec3i vec3i = quad.getFace().vector;
        Vec3f vec3f = new Vec3f(vec3i.x, vec3i.y, vec3i.z);
        Matrix4f matrix4f = matrixEntry.getPositionMatrix();
        vec3f.transform(matrixEntry.getNormalMatrix());
        int j = js.length / 8;
        ByteBuffer byteBuffer = BUFFER;
        byteBuffer.clear();
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        for (int k = 0; k < j; ++k) {
            intBuffer.clear();
            intBuffer.put(js, k * 8, 8);
            float x = byteBuffer.getFloat(0);
            float y = byteBuffer.getFloat(4);
            float z = byteBuffer.getFloat(8);
            float r;
            float g;
            float b;
            if (useQuadColorData) {
                float tmpr = (float) (byteBuffer.get(12) & 0xFF) / 255.0f;
                float tmpg = (float) (byteBuffer.get(13) & 0xFF) / 255.0f;
                float tmpb = (float) (byteBuffer.get(14) & 0xFF) / 255.0f;
                r = tmpr * brightnesses[k] * red;
                g = tmpg * brightnesses[k] * green;
                b = tmpb * brightnesses[k] * blue;
            } else {
                r = brightnesses[k] * red;
                g = brightnesses[k] * green;
                b = brightnesses[k] * blue;
            }
            Vector4f vector4f = new Vector4f(x, y, z, 1.0f);
            vector4f.transform(matrix4f);
            this.vertex(
                    vector4f.getX(), vector4f.getY(), vector4f.getZ(),
                    r, g, b, 1.0f,
                    byteBuffer.getFloat(16), byteBuffer.getFloat(20),
                    overlay,
                    lights[k],
                    vec3f.getX(), vec3f.getY(), vec3f.getZ()
            );
        }
    }

    default VertexConsumer vertex(Matrix4f matrix, float x, float y, float z) {
        Vector4f vector4f = new Vector4f(x, y, z, 1.0f);
        vector4f.transform(matrix);
        return this.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ());
    }

    default VertexConsumer normal(Matrix3f matrix, float x, float y, float z) {
        Vec3f vec3f = new Vec3f(x, y, z);
        vec3f.transform(matrix);
        return this.normal(vec3f.getX(), vec3f.getY(), vec3f.getZ());
    }
}

