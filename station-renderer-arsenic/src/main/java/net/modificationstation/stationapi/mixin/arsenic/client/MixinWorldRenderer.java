package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.class_66;
import net.minecraft.client.render.RenderList;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Living;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.client.gl.VertexBuffer;
import net.modificationstation.stationapi.api.client.render.*;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ChunkBuilderVBO;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow private RenderList[] field_1794;

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/texture/TextureManager;)V",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/client/render/RenderList;"
            )
    )
    private RenderList injectRenderRegion() {
        return new RenderRegion();
    }

    @Inject(
            method = "method_1542(IIID)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderList;method_1910(I)V",
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void addBufferToRegion(int j, int k, int d, double par4, CallbackInfoReturnable<Integer> cir, int var6, Living var7, double var8, double var10, double var12, int var14, int var15, class_66 var16, int var17) {
        ((RenderRegion) this.field_1794[var17]).addBuffer(((ChunkBuilderVBO) var16).getBuffers().get(d == 0 ? RenderLayer.getSolid() : RenderLayer.getTranslucent()));
    }

    @Redirect(
            method = "method_1542(IIID)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/RenderList;method_1910(I)V"
            )
    )
    private void stopCallingRenderList(RenderList instance, int i) {}

    @Inject(
            method = "method_1540(ID)V",
            at = @At("HEAD")
    )
    public void beforeRenderRegion(int d, double par2, CallbackInfo ci) {
        RenderSystem.assertOnRenderThread();
        RenderLayer layer = d == 0 ? RenderLayer.getSolid() : RenderLayer.getTranslucent();
        layer.startDrawing();
        Shader shader = RenderSystem.getShader();
        BufferRenderer.unbindAll();

        if (shader != null) {
            for (int shaderTex = 0; shaderTex < 12; ++shaderTex) {
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
        }
    }

    @Inject(
            method = "method_1540(ID)V",
            at = @At("RETURN")
    )
    public void afterRenderRegion(int d, double par2, CallbackInfo ci) {
        RenderLayer layer = d == 0 ? RenderLayer.getSolid() : RenderLayer.getTranslucent();
        VertexFormat format = layer.getVertexFormat();
        Shader shader = RenderSystem.getShader();
        if (shader != null) {
            shader.unbind();
        }

        format.endDrawing();
        VertexBuffer.unbind();
        VertexBuffer.unbindVertexArray();
        layer.endDrawing();
        GL11.glEnable(2929);
    }
}
