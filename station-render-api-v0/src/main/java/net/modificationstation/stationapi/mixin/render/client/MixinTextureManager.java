package net.modificationstation.stationapi.mixin.render.client;

import jdk.internal.org.objectweb.asm.Opcodes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_214;
import net.minecraft.client.TexturePackManager;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.impl.client.texture.StationTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.awt.image.*;
import java.nio.*;

@Mixin(TextureManager.class)
@Environment(EnvType.CLIENT)
public class MixinTextureManager {

    @Unique
    private final StationTextureManager stationTextureManager = new StationTextureManager((TextureManager) (Object) this);

    @Shadow private ByteBuffer currentImageBuffer;

    @Shadow private TexturePackManager texturePackManager;

    @Inject(
            method = "bindImageToId(Ljava/awt/image/BufferedImage;I)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/texture/TextureManager;currentImageBuffer:Ljava/nio/ByteBuffer;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void method_1089_ensureBufferCapacity(BufferedImage bufferedImage, int i, CallbackInfo ci, int var3, int var4, int[] var5, byte[] var6) {
        if (var6.length != currentImageBuffer.capacity())
            currentImageBuffer = class_214.method_744(var6.length);
    }

    @Inject(
            method = "bindImageToId([IIII)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/texture/TextureManager;currentImageBuffer:Ljava/nio/ByteBuffer;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void method_1095_ensureBufferCapacity(int[] is, int i, int j, int k, CallbackInfo ci, byte[] var5) {
        if (var5.length != currentImageBuffer.capacity())
            currentImageBuffer = class_214.method_744(var5.length);
    }

    @Inject(
            method = "tick()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tick_redirect(CallbackInfo ci) {
        stationTextureManager.tick();
        ci.cancel();
    }
}
