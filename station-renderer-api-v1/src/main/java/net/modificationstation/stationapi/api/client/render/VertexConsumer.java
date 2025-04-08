package net.modificationstation.stationapi.api.client.render;

import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.math.ColorHelper;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * An interface that consumes vertices in a certain {@linkplain
 * VertexFormat vertex format}.
 *
 * <p>The vertex elements must be specified in the same order as defined in
 * the format the vertices being consumed are in.
 */
public interface VertexConsumer {
    /**
     * Specifies the {@linkplain VertexFormatElement#POSITION
     * position element} of the current vertex.
     *
     * <p>This is typically the first element in a vertex, hence the name.
     *
     * @throws IllegalStateException if this consumer is not currently
     * accepting a position element.
     *
     * @return this consumer, for chaining
     */
    VertexConsumer setVertex(float x, float y, float z);

    /**
     * Specifies the {@linkplain VertexFormatElement#COLOR
     * color element} of the current vertex.
     *
     * @throws IllegalStateException if this consumer is not currently
     * accepting a color element.
     *
     * @return this consumer, for chaining
     */
    VertexConsumer setColor(int red, int green, int blue, int alpha);

    /**
     * Specifies the {@linkplain VertexFormatElement#UV0
     * texture element} of the current vertex.
     *
     * @throws IllegalStateException if this consumer is not currently
     * accepting a texture element.
     *
     * @return this consumer, for chaining
     */
    VertexConsumer setTexture(float u, float v);

    /**
     * Specifies the {@linkplain VertexFormatElement#UV1
     * overlay element} of the current vertex.
     *
     * @throws IllegalStateException if this consumer is not currently
     * accepting an overlay element.
     *
     * @return this consumer, for chaining
     */
    VertexConsumer setOverlay(int u, int v);

    /**
     * Specifies the {@linkplain VertexFormatElement#UV2
     * light element} of the current vertex.
     *
     * @throws IllegalStateException if this consumer is not currently
     * accepting a light element.
     *
     * @return this consumer, for chaining
     */
    VertexConsumer setLight(int u, int v);

    VertexConsumer setNormal(float x, float y, float z);

    default void vertex(float x, float y, float z, int color, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
        setVertex(x, y, z);
        setColor(color);
        setTexture(u, v);
//        setOverlay(overlay);
//        setLight(light);
        setNormal(normalX, normalY, normalZ);
    }

    default void vertex(float x, float y, float z, int color, float u, float v, float normalX, float normalY, float normalZ) {
        setVertex(x, y, z);
        setColor(color);
        setTexture(u, v);
        setNormal(normalX, normalY, normalZ);
    }

    default VertexConsumer setColor(float red, float green, float blue, float alpha) {
        return this.setColor((int)(red * 255.0F), (int)(green * 255.0F), (int)(blue * 255.0F), (int)(alpha * 255.0F));
    }

    default VertexConsumer setColor(int argb) {
        return this.setColor(ColorHelper.Abgr.getRed(argb), ColorHelper.Abgr.getGreen(argb), ColorHelper.Abgr.getBlue(argb), ColorHelper.Abgr.getAlpha(argb));
    }

    default VertexConsumer setColorRgb(int rgb) {
        return this.setColor(ColorHelper.Abgr.withAlpha(rgb, -1));
    }


    // TODO
    default VertexConsumer quad(MatrixStack.Entry entry, BakedQuad quad, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        int[] vertexData = quad.vertexData();

        Vector3fc faceNormal = quad.face().getFloatVector();
        Matrix4f pos = entry.getPositionMatrix();
        Vector3f normal = entry.transformNormal(faceNormal, new Vector3f());
        return this;
    }

    default void quad(BakedQuad quad, float x, float y, float z, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        Util.assertImpl();
    }
}
