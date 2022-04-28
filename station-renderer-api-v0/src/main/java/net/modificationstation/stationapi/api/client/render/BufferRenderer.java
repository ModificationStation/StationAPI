package net.modificationstation.stationapi.api.client.render;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.blaze3d.platform.GlStateManager;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;

import java.nio.ByteBuffer;
import java.util.Objects;

@Environment(value=EnvType.CLIENT)
public class BufferRenderer {
    private static int currentVertexArray;
    private static int currentVertexBuffer;
    private static int currentElementBuffer;
    @Nullable
    private static VertexFormat vertexFormat;

    public static void unbindAll() {
        if (vertexFormat != null) {
            vertexFormat.endDrawing();
            vertexFormat = null;
        }
        GlStateManager._glBindBuffer(34963, 0);
        currentElementBuffer = 0;
        GlStateManager._glBindBuffer(34962, 0);
        currentVertexBuffer = 0;
        GlStateManager._glBindVertexArray(0);
        currentVertexArray = 0;
    }

    public static void unbindElementBuffer() {
        GlStateManager._glBindBuffer(34963, 0);
        currentElementBuffer = 0;
    }

    public static void draw(BufferBuilder bufferBuilder) {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> {
                Pair<BufferBuilder.DrawArrayParameters, ByteBuffer> pair = bufferBuilder.popData();
                BufferBuilder.DrawArrayParameters drawArrayParameters = pair.getFirst();
                BufferRenderer.draw(pair.getSecond(), drawArrayParameters.getMode(), drawArrayParameters.getVertexFormat(), drawArrayParameters.getCount(), drawArrayParameters.getElementFormat(), drawArrayParameters.getVertexCount(), drawArrayParameters.hasNoIndexBuffer());
            });
        } else {
            Pair<BufferBuilder.DrawArrayParameters, ByteBuffer> pair = bufferBuilder.popData();
            BufferBuilder.DrawArrayParameters drawArrayParameters = pair.getFirst();
            BufferRenderer.draw(pair.getSecond(), drawArrayParameters.getMode(), drawArrayParameters.getVertexFormat(), drawArrayParameters.getCount(), drawArrayParameters.getElementFormat(), drawArrayParameters.getVertexCount(), drawArrayParameters.hasNoIndexBuffer());
        }
    }

    private static void draw(ByteBuffer buffer, VertexFormat.DrawMode drawMode, VertexFormat vertexFormat, int count, VertexFormat.IntType elementFormat, int vertexCount, boolean textured) {
        int k;
        int j;
        RenderSystem.assertOnRenderThread();
        buffer.clear();
        if (count <= 0) {
            return;
        }
        int i = count * vertexFormat.getVertexSize();
        BufferRenderer.bind(vertexFormat);
        buffer.position(0);
        buffer.limit(i);
        GlStateManager._glBufferData(34962, buffer, 35048);
        if (textured) {
            RenderSystem.IndexBuffer indexBuffer = RenderSystem.getSequentialBuffer(drawMode, vertexCount);
            j = indexBuffer.getId();
            if (j != currentElementBuffer) {
                GlStateManager._glBindBuffer(34963, j);
                currentElementBuffer = j;
            }
            k = indexBuffer.getElementFormat().type;
        } else {
            int l = vertexFormat.getElementBuffer();
            if (l != currentElementBuffer) {
                GlStateManager._glBindBuffer(34963, l);
                currentElementBuffer = l;
            }
            buffer.position(i);
            buffer.limit(i + vertexCount * elementFormat.size);
            GlStateManager._glBufferData(34963, buffer, 35048);
            k = elementFormat.type;
        }
        Shader shader = RenderSystem.getShader();
        for (j = 0; j < 8; ++j) {
            int m = RenderSystem.getShaderTexture(j);
            Objects.requireNonNull(shader).addSampler("Sampler" + j, m);
        }
        if (shader.modelViewMat != null) {
            shader.modelViewMat.set(RenderSystem.getModelViewMatrix());
        }
        if (shader.projectionMat != null) {
            shader.projectionMat.set(RenderSystem.getProjectionMatrix());
        }
        if (shader.viewRotationMat != null) {
            shader.viewRotationMat.set(RenderSystem.getInverseViewRotationMatrix());
        }
        if (shader.colorModulator != null) {
            shader.colorModulator.set(RenderSystem.getShaderColor());
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
        if (shader.lineWidth != null && (drawMode == VertexFormat.DrawMode.LINES || drawMode == VertexFormat.DrawMode.LINE_STRIP)) {
            shader.lineWidth.set(RenderSystem.getShaderLineWidth());
        }
        RenderSystem.setupShaderLights(shader);
        shader.bind();
        GlStateManager._drawElements(drawMode.mode, vertexCount, k, 0L);
        shader.unbind();
        buffer.position(0);
    }

    /**
     * Similar to a regular draw, however this method will skip rendering shaders.
     */
    public static void postDraw(BufferBuilder builder) {
        RenderSystem.assertOnRenderThread();
        Pair<BufferBuilder.DrawArrayParameters, ByteBuffer> pair = builder.popData();
        BufferBuilder.DrawArrayParameters drawArrayParameters = pair.getFirst();
        ByteBuffer byteBuffer = pair.getSecond();
        VertexFormat vertexFormat = drawArrayParameters.getVertexFormat();
        int i = drawArrayParameters.getCount();
        byteBuffer.clear();
        if (i <= 0) {
            return;
        }
        int j = i * vertexFormat.getVertexSize();
        BufferRenderer.bind(vertexFormat);
        byteBuffer.position(0);
        byteBuffer.limit(j);
        GlStateManager._glBufferData(34962, byteBuffer, 35048);
        RenderSystem.IndexBuffer indexBuffer = RenderSystem.getSequentialBuffer(drawArrayParameters.getMode(), drawArrayParameters.getVertexCount());
        int k = indexBuffer.getId();
        if (k != currentElementBuffer) {
            GlStateManager._glBindBuffer(34963, k);
            currentElementBuffer = k;
        }
        int l = indexBuffer.getElementFormat().type;
        GlStateManager._drawElements(drawArrayParameters.getMode().mode, drawArrayParameters.getVertexCount(), l, 0L);
        byteBuffer.position(0);
    }

    private static void bind(VertexFormat vertexFormat) {
        boolean bl;
        int i = vertexFormat.getVertexArray();
        int j = vertexFormat.getVertexBuffer();
        bl = vertexFormat != BufferRenderer.vertexFormat;
        if (bl) {
            BufferRenderer.unbindAll();
        }
        if (i != currentVertexArray) {
            GlStateManager._glBindVertexArray(i);
            currentVertexArray = i;
        }
        if (j != currentVertexBuffer) {
            GlStateManager._glBindBuffer(34962, j);
            currentVertexBuffer = j;
        }
        if (bl) {
            vertexFormat.startDrawing();
            BufferRenderer.vertexFormat = vertexFormat;
        }
    }
}

