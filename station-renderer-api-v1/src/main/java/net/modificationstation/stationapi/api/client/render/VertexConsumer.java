package net.modificationstation.stationapi.api.client.render;

import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public interface VertexConsumer {
    default void vertex(double x, double y, double z) {
        vertex((float) x, (float) y, (float) z);
    }

    void vertex(float x, float y, float z);

    void color(int red, int green, int blue, int alpha);

    void texture(double u, double v);

    void normal(float x, float y, float z);

    // TODO
    default VertexConsumer quad(MatrixStack.Entry entry, BakedQuad quad, int colour0, int colour1, int colour2, int colour3, float normalX, float normalY, float normalZ, boolean spreadUV) {
        int[] vertexData = quad.vertexData();

        Vector3fc faceNormal = quad.face().getFloatVector();
        Matrix4f pos = entry.getPositionMatrix();
        Vector3f normal = entry.transformNormal(faceNormal, new Vector3f());
        return this;
    }
}
