package net.modificationstation.stationapi.impl.client.arsenic.renderer.render;

import net.modificationstation.stationapi.api.client.render.VertexConsumer;
import net.modificationstation.stationapi.api.client.render.mesh.QuadEmitter;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.EncodingFormat;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.mesh.MutableQuadViewImpl;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public abstract class AbstractRenderContext {
    private final MutableQuadViewImpl editorQuad = new MutableQuadViewImpl() {
        {
            data = new int[EncodingFormat.TOTAL_STRIDE];
            clear();
        }

        @Override
        protected void emitDirectly() {
            bufferQuad(this);
        }
    };

    private final Vector4f posVec = new Vector4f();
    private final Vector3f normalVec = new Vector3f();

    protected MatrixStack.Entry matrices;

    protected QuadEmitter getEmitter() {
        editorQuad.clear();
        return editorQuad;
    }

    protected abstract void bufferQuad(MutableQuadViewImpl quad);

    /** final output step, common to all renders. */
    protected void bufferQuad(MutableQuadViewImpl quad, VertexConsumer vertexConsumer) {
        final Vector4f posVec = this.posVec;
        final Vector3f normalVec = this.normalVec;
        final MatrixStack.Entry matrices = this.matrices;
        final Matrix4f posMatrix = matrices.getPositionMatrix();
        final boolean useNormals = quad.hasVertexNormals();

        if (useNormals) {
            quad.populateMissingNormals();
        } else {
            matrices.transformNormal(quad.faceNormal(), normalVec);
        }

        for (int i = 0; i < 4; i++) {
            posVec.set(quad.x(i), quad.y(i), quad.z(i), 1.0f);
            posVec.mul(posMatrix);

            if (useNormals) {
                quad.copyNormal(i, normalVec);
                matrices.transformNormal(normalVec, normalVec);
            }

            vertexConsumer.vertex(posVec.x(), posVec.y(), posVec.z(), quad.color(i), quad.u(i), quad.v(i), normalVec.x(), normalVec.y(), normalVec.z());
        }
    }
}
