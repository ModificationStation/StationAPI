package net.modificationstation.stationapi.mixin.render.client;

import jdk.internal.org.objectweb.asm.Opcodes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_214;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.texture.AfterTexturePackLoadedEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.binder.StaticReferenceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import uk.co.benjiweber.expressions.Times;

import java.awt.image.*;
import java.nio.*;
import java.util.*;

@Mixin(TextureManager.class)
@Environment(EnvType.CLIENT)
public class MixinTextureManager {

    @Shadow private ByteBuffer field_1250;

    @Inject(
            method = "method_1089(Ljava/awt/image/BufferedImage;I)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/texture/TextureManager;field_1250:Ljava/nio/ByteBuffer;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void method_1089_ensureBufferCapacity(BufferedImage bufferedImage, int i, CallbackInfo ci, int var3, int var4, int[] var5, byte[] var6) {
        if (var6.length != field_1250.capacity())
            field_1250 = class_214.method_744(var6.length);
    }

    @Inject(
            method = "method_1095([IIII)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/texture/TextureManager;field_1250:Ljava/nio/ByteBuffer;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void method_1095_ensureBufferCapacity(int[] is, int i, int j, int k, CallbackInfo ci, byte[] var5) {
        if (var5.length != field_1250.capacity())
            field_1250 = class_214.method_744(var5.length);
    }

    @Inject(
            method = "tick()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/texture/TextureManager;field_1250:Ljava/nio/ByteBuffer;",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BEFORE
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void tick_ensureBufferCapacity(CallbackInfo ci, int var1, TextureBinder var2) {
        if (var2.grid.length != field_1250.capacity())
            field_1250 = class_214.method_744(var2.grid.length);
    }

    @Unique
    private final Stack<Atlas.Texture> tick_customLocals_staticReference = new Stack<>();

    @ModifyVariable(
            method = "tick()V",
            index = 2,
            at = @At("STORE")
    )
    private TextureBinder captureTextureBinder(TextureBinder textureBinder) {
        Times.times(textureBinder.textureSize * textureBinder.textureSize).invoke(() -> tick_customLocals_staticReference.push(textureBinder instanceof StaticReferenceProvider ? ((StaticReferenceProvider) textureBinder).getStaticReference() : null));
        return textureBinder;
    }

    @ModifyArg(
            method = "tick()V",
            index = 2,
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexSubImage2D(IIIIIIIILjava/nio/ByteBuffer;)V",
                    ordinal = 0
            )
    )
    private int modifyXOffset(int originalXOffset) {
        Atlas.Texture staticReference = tick_customLocals_staticReference.peek();
        return staticReference == null ? originalXOffset : staticReference.getX();
    }

    @ModifyArg(
            method = "tick()V",
            index = 3,
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexSubImage2D(IIIIIIIILjava/nio/ByteBuffer;)V",
                    ordinal = 0
            )
    )
    private int modifyYOffset(int originalYOffset) {
        Atlas.Texture staticReference = tick_customLocals_staticReference.peek();
        return staticReference == null ? originalYOffset : staticReference.getY();
    }

    @ModifyArg(
            method = "tick()V",
            index = 4,
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexSubImage2D(IIIIIIIILjava/nio/ByteBuffer;)V",
                    ordinal = 0
            )
    )
    private int modifyWidth(int originalWidth) {
        Atlas.Texture staticReference = tick_customLocals_staticReference.peek();
        return staticReference == null ? originalWidth : staticReference.getWidth();
    }

    @ModifyArg(
            method = "tick()V",
            index = 5,
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/GL11;glTexSubImage2D(IIIIIIIILjava/nio/ByteBuffer;)V",
                    ordinal = 0
            )
    )
    private int modifyHeight(int originalHeight) {
        Atlas.Texture staticReference = tick_customLocals_staticReference.pop();
        return staticReference == null ? originalHeight : staticReference.getHeight();
    }

    @Inject(
            method = "method_1096()V",
            at = @At("RETURN")
    )
    private void texturesRefresh(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new AfterTexturePackLoadedEvent());
    }
}
