package net.modificationstation.stationapi.mixin.arsenic.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.texture.DynamicTexture;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.SpriteContents;
import net.modificationstation.stationapi.api.client.texture.StationTextureManager;
import net.modificationstation.stationapi.api.client.texture.TextureTickListener;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.binder.StaticReferenceProvider;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicTextureManager;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.image.BufferedImage;

@Mixin(TextureManager.class)
@Environment(EnvType.CLIENT)
class TextureManagerMixin {
    @SuppressWarnings("DataFlowIssue")
    @Unique
    private final ArsenicTextureManager arsenic_plugin = new ArsenicTextureManager((TextureManager) (Object) this);

    @Inject(
            method = "getTextureId(Ljava/lang/String;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_getTextureId(String par1, CallbackInfoReturnable<Integer> cir) {
        arsenic_plugin.getTextureId(par1, cir);
    }

    @Inject(
            method = "method_1089",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/nio/ByteBuffer;clear()Ljava/nio/Buffer;",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_bindBufferedImageToId_ensureBufferCapacity(BufferedImage i, int par2, CallbackInfo ci, int var3, int var4, int[] var5, byte[] var6) {
        arsenic_plugin.ensureBufferCapacity(var6.length);
    }

    @Inject(
            method = "method_1095",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/nio/ByteBuffer;clear()Ljava/nio/Buffer;",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_bindArrayImageToId_ensureBufferCapacity(int[] i, int j, int k, int par4, CallbackInfo ci, byte[] var5) {
        arsenic_plugin.ensureBufferCapacity(var5.length);
    }

    @Inject(
            method = "method_1084",
            at = @At("HEAD")
    )
    private void stationapi_tick_onTickStart(CallbackInfo ci) {
        //noinspection DataFlowIssue
        StationTextureManager.get((TextureManager) (Object) this).getTickListeners().forEach(TextureTickListener::tick);
        StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).bindTexture();
        GL11.glPixelStorei(3314, 0);
        GL11.glPixelStorei(3315, 0);
        GL11.glPixelStorei(3316, 0);
    }

    @Inject(
            method = "method_1084",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/nio/ByteBuffer;clear()Ljava/nio/Buffer;",
                    shift = At.Shift.AFTER,
                    remap = false
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_tick_ensureBufferCapacity(CallbackInfo ci, int var1, DynamicTexture var2) {
        arsenic_plugin.ensureBufferCapacity(var2.pixels.length);
    }

    @Redirect(
            method = "method_1084",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_336;method_1206(Lnet/minecraft/client/texture/TextureManager;)V"
            )
    )
    private void stationapi_tick_preventBindingTextureAndCheckCustomBinder(DynamicTexture instance, TextureManager manager) {
        if (instance instanceof StaticReferenceProvider provider) {
            final Sprite staticReference = provider.getStaticReference().getSprite();
            final SpriteContents contents = staticReference.getContents();
            stationapi_customBinder = true;
            stationapi_customBinderX = staticReference.getX();
            stationapi_customBinderY = staticReference.getY();
            stationapi_binderScaledWidth = contents.getWidth() / instance.replicate;
            stationapi_binderScaledHeight = contents.getHeight() / instance.replicate;
        } else stationapi_customBinder = false;
    }

    @Unique
    private boolean stationapi_customBinder;
    @Unique
    private int
            stationapi_customBinderX,
            stationapi_customBinderY,
            stationapi_binderXOffset,
            stationapi_binderYOffset,
            stationapi_binderScaledWidth,
            stationapi_binderScaledHeight;

    @Inject(
            method = "method_1084",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=3553",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_tick_captureCurrentBinderOffsets(CallbackInfo ci, int var1, DynamicTexture var2, int var3, int var4) {
        stationapi_binderXOffset = var3;
        stationapi_binderYOffset = var4;
    }

    @ModifyArg(
            method = "method_1084",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexSubImage2D(IIIIIIIILjava/nio/ByteBuffer;)V",
                    ordinal = 0,
                    remap = false
            ),
            index = 2
    )
    private int stationapi_tick_modBinderX(int xoffset) {
        return stationapi_customBinder ? stationapi_customBinderX + stationapi_binderXOffset * stationapi_binderScaledWidth : xoffset;
    }

    @ModifyArg(
            method = "method_1084",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexSubImage2D(IIIIIIIILjava/nio/ByteBuffer;)V",
                    ordinal = 0,
                    remap = false
            ),
            index = 3
    )
    private int stationapi_tick_modBinderY(int yoffset) {
        return stationapi_customBinder ? stationapi_customBinderY + stationapi_binderYOffset * stationapi_binderScaledHeight : yoffset;
    }

    @ModifyArg(
            method = "method_1084",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexSubImage2D(IIIIIIIILjava/nio/ByteBuffer;)V",
                    ordinal = 0,
                    remap = false
            ),
            index = 4
    )
    private int stationapi_tick_modBinderScaledWidth(int scaledWidth) {
        return stationapi_customBinder ? stationapi_binderScaledWidth : scaledWidth;
    }

    @ModifyArg(
            method = "method_1084",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexSubImage2D(IIIIIIIILjava/nio/ByteBuffer;)V",
                    ordinal = 0,
                    remap = false
            ),
            index = 5
    )
    private int stationapi_tick_modBinderScaledHeight(int scaledHeight) {
        return stationapi_customBinder ? stationapi_binderScaledHeight : scaledHeight;
    }
}
