package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.block.BlockBase;
import net.minecraft.class_66;
import net.minecraft.client.render.RenderList;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import net.modificationstation.stationapi.api.client.gl.VertexBuffer;
import net.modificationstation.stationapi.api.client.render.*;
import net.modificationstation.stationapi.api.client.render.model.ModelLoader;
import net.modificationstation.stationapi.api.level.BlockStateView;
import net.modificationstation.stationapi.api.util.math.MatrixStack;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ChunkBuilderVBO;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {

    @Shadow private RenderList[] field_1794;
    @Shadow private Level level;

    private static final BufferBuilderStorage bufferBuilders = new BufferBuilderStorage();
    @Unique
    private final MatrixStack damageMtrx = new MatrixStack();

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<init>(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/texture/TextureManager;)V",
            at = @At(
                    value = "NEW",
                    target = "()Lnet/minecraft/client/render/RenderList;"
            )
    )
    private RenderList injectRenderRegion() {
        return new RenderRegion((WorldRenderer) (Object) this);
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

        format.clearState();
        VertexBuffer.unbind();
//        VertexBuffer.unbindVertexArray();
        layer.endDrawing();
        GL11.glEnable(2929);
    }

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;start()V"
            )
    )
    private void begoneTessellator1(Tessellator instance) {}

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;setOffset(DDD)V"
            )
    )
    private void begoneTessellator2(Tessellator instance, double e, double f, double v) {}

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;disableColour()V"
            )
    )
    private void begoneTessellator3(Tessellator instance) {}

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;draw()V"
            )
    )
    private void begoneTessellator4(Tessellator instance) {}

    @Redirect(
            method = "method_1547(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;renderWithTexture(Lnet/minecraft/block/BlockBase;IIII)V"
            )
    )
    private void renderDamage(BlockRenderer instance, BlockBase block, int j, int k, int l, int texture, PlayerBase arg, HitResult arg2, int i, ItemInstance arg3, float f) {
        damageMtrx.push();
        double d = arg.prevRenderX + (arg.x - arg.prevRenderX) * (double)f;
        double d2 = arg.prevRenderY + (arg.y - arg.prevRenderY) * (double)f;
        double d3 = arg.prevRenderZ + (arg.z - arg.prevRenderZ) * (double)f;
        damageMtrx.translate(-d, -d2, -d3);
        MatrixStack.Entry entry = damageMtrx.peek();
        OverlayVertexConsumer vertexConsumer = new OverlayVertexConsumer(bufferBuilders.getEffectVertexConsumers().getBuffer(ModelLoader.BLOCK_DESTRUCTION_RENDER_LAYERS.get(texture - 240)), entry.getPositionMatrix(), entry.getNormalMatrix());
        BlockState state = ((BlockStateView) level).getBlockState(j, k, l);
        Objects.requireNonNull(RendererAccess.INSTANCE.getRenderer()).bakedModelRenderer().renderDamage(state, new TilePos(j, k, l), level, damageMtrx, vertexConsumer);
        damageMtrx.pop();
        bufferBuilders.getEffectVertexConsumers().draw();
//        VertexBuffer.unbindVertexArray();
        VertexBuffer.unbind();
        GL11.glEnable(2929);
    }
}
