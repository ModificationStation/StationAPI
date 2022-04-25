package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.class_66;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderList;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.client.gl.GlUniform;
import net.modificationstation.stationapi.api.client.gl.VertexBuffer;
import net.modificationstation.stationapi.api.client.render.BufferRenderer;
import net.modificationstation.stationapi.api.client.render.RenderLayer;
import net.modificationstation.stationapi.api.client.render.Shader;
import net.modificationstation.stationapi.api.client.render.VertexFormat;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ChunkBuilderVBO;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow private Minecraft client;

    @Shadow private class_66[] field_1808;

    @Shadow private boolean field_1817;

    @Shadow private List<class_66> field_1793;

    @Shadow private int field_1787;

    @Shadow private int field_1791;

    @Shadow private int field_1788;

    @Shadow private int field_1789;

    @Shadow private int field_1790;

    @Shadow private RenderList[] field_1794;

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    private int method_1542(int i, int j, int k, double d) {
        int n;
        this.field_1793.clear();
        int n2 = 0;
        for (int i2 = i; i2 < j; ++i2) {
            if (k == 0) {
                ++this.field_1787;
                if (this.field_1808[i2].field_244[k]) {
                    ++this.field_1791;
                } else if (!this.field_1808[i2].field_243) {
                    ++this.field_1788;
                } else if (this.field_1817 && !this.field_1808[i2].field_252) {
                    ++this.field_1789;
                } else {
                    ++this.field_1790;
                }
            }
            if (this.field_1808[i2].field_244[k] || !this.field_1808[i2].field_243 || this.field_1817 && !this.field_1808[i2].field_252 || this.field_1808[i2].method_297(k) < 0) continue;
            this.field_1793.add(this.field_1808[i2]);
            ++n2;
        }
        PlayerBase living = (PlayerBase) this.client.viewEntity;
        double d2 = living.prevRenderX + (living.x - living.prevRenderX) * d;
        double d3 = living.prevRenderY + (living.y - living.prevRenderY) * d;
        double d4 = living.prevRenderZ + (living.z - living.prevRenderZ) * d;
        int n4 = 0;
        for (n = 0; n < this.field_1794.length; ++n) {
            this.field_1794[n].method_1913();
        }

        RenderSystem.assertOnRenderThread();
        RenderLayer layer = k == 0 ? RenderLayer.getSolid() : RenderLayer.getTranslucent();
        layer.startDrawing();
        VertexFormat format = layer.getVertexFormat();
        Shader shader = RenderSystem.getShader();
        BufferRenderer.unbindAll();

        for(int shaderTex = 0; shaderTex < 12; ++shaderTex) {
            int l = RenderSystem.getShaderTexture(shaderTex);
            shader.addSampler("Sampler" + shaderTex, l);
        }

        if (shader.modelViewMat != null) {
            shader.modelViewMat.set(RenderSystem.getModelViewMatrix());
        }

        if (shader.projectionMat != null) {
            shader.projectionMat.set(RenderSystem.getProjectionMatrix());
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

        RenderSystem.setupShaderLights(shader);
        shader.bind();
        GlUniform glUniform = shader.chunkOffset;

        boolean rendered = false;
        for (n = 0; n < this.field_1793.size(); ++n) {
            class_66 class_662 = this.field_1793.get(n);
            int n5 = -1;
            for (int i3 = 0; i3 < n4; ++i3) {
                if (!this.field_1794[i3].method_1911(class_662.field_237, class_662.field_238, class_662.field_239)) continue;
                n5 = i3;
            }
            if (n5 < 0) {
                n5 = n4++;
                this.field_1794[n5].method_1912(class_662.field_237, class_662.field_238, class_662.field_239, d2, d3, d4);
            }
            RenderListAccessor renderList = (RenderListAccessor) this.field_1794[n5];
            VertexBuffer vbo = ((ChunkBuilderVBO) class_662).getBuffers().get(layer);
            if (glUniform != null) {
                glUniform.set(renderList.stationapi$getField_2480() - renderList.stationapi$getField_2483(), renderList.stationapi$getField_2481() - renderList.stationapi$getField_2484(), renderList.stationapi$getField_2482() - renderList.stationapi$getField_2485());
                glUniform.upload();
            }
            vbo.drawVertices();
            rendered = true;
//            this.field_1794[n5].method_1910(class_662.method_297(k));
        }

        if (glUniform != null) {
            glUniform.set(Vec3f.ZERO);
        }

        shader.unbind();

        if (rendered) {
            format.endDrawing();
        }
        VertexBuffer.unbind();
        VertexBuffer.unbindVertexArray();
        layer.endDrawing();
        this.method_1540(k, d);
        GL11.glEnable(2929);
        return n2;
    }

    /**
     * @reason early version
     * @author mine_diver
     */
    @Overwrite
    public void method_1540(int i, double d) {

    }
}
