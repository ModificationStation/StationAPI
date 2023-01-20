package net.modificationstation.stationapi.api.client.gl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlConst;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlStateManager;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.client.render.*;
import net.modificationstation.stationapi.api.util.math.Matrix4f;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;

@Environment(value=EnvType.CLIENT)
public class VertexBuffer
implements AutoCloseable {
    private int vertexBufferId;
    private int indexBufferId;
    private int vertexArrayId;
    @Nullable
    private VertexFormat vertexFormat;
    @Nullable
    private RenderSystem.IndexBuffer indexBuffer;
    private VertexFormat.IndexType indexType;
    private int vertexCount;
    private VertexFormat.DrawMode drawMode;
    private VboPool pool;
    private VboPool.Pos poolPos;

    public VertexBuffer() {
        RenderSystem.assertOnRenderThread();
        this.vertexBufferId = GlStateManager._glGenBuffers();
        this.indexBufferId = GlStateManager._glGenBuffers();
        this.vertexArrayId = GlStateManager._glGenVertexArrays();
    }

    public VertexBuffer(VboPool pool) {
        this.pool = pool;
        this.poolPos = new VboPool.Pos();
    }

    public void upload(BufferBuilder.BuiltBuffer buffer) {
        if (this.isClosed()) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        try {
            BufferBuilder.DrawArrayParameters drawArrayParameters = buffer.getParameters();
            if (pool == null) {
                this.vertexFormat = this.configureVertexFormat(drawArrayParameters, buffer.getVertexBuffer());
                this.indexBuffer = this.configureIndexBuffer(drawArrayParameters, buffer.getIndexBuffer());
                this.vertexCount = drawArrayParameters.indexCount();
                this.indexType = drawArrayParameters.indexType();
                this.drawMode = drawArrayParameters.mode();
            } else {
                ByteBuffer bytebuffer1 = buffer.getVertexBuffer();
//                bytebuffer1.position(0);
//                bytebuffer1.limit(drawArrayParameters.getIndexBufferStart());
                this.pool.bufferData(bytebuffer1, this.poolPos);
//                bytebuffer1.position(0);
//                bytebuffer1.limit(drawArrayParameters.getIndexBufferEnd());
            }
        }
        finally {
            buffer.release();
        }
    }

    private VertexFormat configureVertexFormat(BufferBuilder.DrawArrayParameters parameters, ByteBuffer data) {
        boolean bl = false;
        if (!parameters.format().equals(this.vertexFormat)) {
            if (this.vertexFormat != null) {
                this.vertexFormat.clearState();
            }
            GlStateManager._glBindBuffer(GlConst.GL_ARRAY_BUFFER, this.vertexBufferId);
            parameters.format().setupState();
            bl = true;
        }
        if (!parameters.indexOnly()) {
            if (!bl) {
                GlStateManager._glBindBuffer(GlConst.GL_ARRAY_BUFFER, this.vertexBufferId);
            }
            RenderSystem.glBufferData(GlConst.GL_ARRAY_BUFFER, data, GlConst.GL_STATIC_DRAW);
        }
        return parameters.format();
    }

    @Nullable
    private RenderSystem.IndexBuffer configureIndexBuffer(BufferBuilder.DrawArrayParameters parameters, ByteBuffer data) {
        if (parameters.sequentialIndex()) {
            RenderSystem.IndexBuffer indexBuffer = RenderSystem.getSequentialBuffer(parameters.mode());
            if (indexBuffer != this.indexBuffer || !indexBuffer.isSizeLessThanOrEqual(parameters.indexCount())) {
                indexBuffer.bindAndGrow(parameters.indexCount());
            }
            return indexBuffer;
        }
        GlStateManager._glBindBuffer(GlConst.GL_ELEMENT_ARRAY_BUFFER, this.indexBufferId);
        RenderSystem.glBufferData(GlConst.GL_ELEMENT_ARRAY_BUFFER, data, GlConst.GL_STATIC_DRAW);
        return null;
    }

    public void bind() {
        BufferRenderer.resetCurrentVertexBuffer();
        GlStateManager._glBindVertexArray(this.vertexArrayId);
    }

    public static void unbind() {
        BufferRenderer.resetCurrentVertexBuffer();
        GlStateManager._glBindVertexArray(0);
        GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GlStateManager._glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void drawElements() {
        RenderSystem.drawElements(this.drawMode.glMode, this.vertexCount, this.getIndexType().glType);
    }

    private VertexFormat.IndexType getIndexType() {
        RenderSystem.IndexBuffer indexBuffer = this.indexBuffer;
        return indexBuffer != null ? indexBuffer.getIndexType() : this.indexType;
    }

    public void draw(Matrix4f viewMatrix, Matrix4f projectionMatrix, Shader shader) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this.drawInternal(viewMatrix.copy(), projectionMatrix.copy(), shader));
        } else {
            this.drawInternal(viewMatrix, projectionMatrix, shader);
        }
    }

    private void drawInternal(Matrix4f viewMatrix, Matrix4f projectionMatrix, Shader shader) {
        for (int i = 0; i < 12; ++i) {
            int j = RenderSystem.getShaderTexture(i);
            shader.addSampler("Sampler" + i, j);
        }
        if (shader.modelViewMat != null) {
            shader.modelViewMat.set(viewMatrix);
        }
        if (shader.projectionMat != null) {
            shader.projectionMat.set(projectionMatrix);
        }
        if (shader.viewRotationMat != null) {
            shader.viewRotationMat.set(RenderSystem.getInverseViewRotationMatrix());
        }
        if (shader.colorModulator != null) {
            shader.colorModulator.set(RenderSystem.getShaderColor());
        }
        if (shader.fogMode != null) {
            shader.fogMode.set(RenderSystem.getShaderFogMode().getId());
        }
        if (shader.fogDensity != null) {
            shader.fogDensity.set(RenderSystem.getShaderFogDensity());
        }
        if (shader.fogStart != null) {
            shader.fogStart.set(RenderSystem.getShaderFogStart());
        }
        if (shader.fogEnd != null) {
            shader.fogEnd.set(RenderSystem.getShaderFogEnd());
        }
        if (shader.fogColor != null) {
            shader.fogColor.set(RenderSystem.getShaderFogColor());
        }
        if (shader.fogShape != null) {
            shader.fogShape.set(RenderSystem.getShaderFogShape().getId());
        }
        if (shader.textureMat != null) {
            shader.textureMat.set(RenderSystem.getTextureMatrix());
        }
        if (shader.gameTime != null) {
            shader.gameTime.set(RenderSystem.getShaderGameTime());
        }
        if (shader.screenSize != null) {
            shader.screenSize.set((float) Display.getWidth(), (float) Display.getHeight());
        }
        if (shader.lineWidth != null && (this.drawMode == VertexFormat.DrawMode.LINES || this.drawMode == VertexFormat.DrawMode.LINE_STRIP)) {
            shader.lineWidth.set(RenderSystem.getShaderLineWidth());
        }
        RenderSystem.setupShaderLights(shader);
        shader.bind();
        this.drawElements();
        shader.unbind();
    }

    public void uploadToPool() {
        if (this.pool != null) {
            this.pool.upload(VertexFormat.DrawMode.QUADS, this.poolPos);
        }
    }

    @Override
    public void close() {
        if (this.vertexBufferId >= 0) {
            RenderSystem.glDeleteBuffers(this.vertexBufferId);
            this.vertexBufferId = -1;
        }
        if (this.indexBufferId >= 0) {
            RenderSystem.glDeleteBuffers(this.indexBufferId);
            this.indexBufferId = -1;
        }
        if (this.vertexArrayId >= 0) {
            RenderSystem.glDeleteVertexArrays(this.vertexArrayId);
            this.vertexArrayId = -1;
        }
    }

    public VertexFormat getVertexFormat() {
        return this.vertexFormat;
    }

    public boolean isClosed() {
        return this.vertexArrayId == -1;
    }
}

