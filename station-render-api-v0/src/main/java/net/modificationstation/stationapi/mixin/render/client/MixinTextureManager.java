package net.modificationstation.stationapi.mixin.render.client;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.plugin.RenderPlugin;
import net.modificationstation.stationapi.api.client.texture.plugin.TextureManagerPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.image.*;

@Mixin(TextureManager.class)
@Environment(EnvType.CLIENT)
public class MixinTextureManager implements TextureManagerPlugin.Provider {

    @Unique
    @Getter
    private final TextureManagerPlugin plugin = RenderPlugin.PLUGIN.createTextureManager((TextureManager) (Object) this);

    @Inject(
            method = "bindImageToId(Ljava/awt/image/BufferedImage;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void method_1089_ensureBufferCapacity(BufferedImage image, int targetId, CallbackInfo ci) {
        plugin.bindImageToId(image, targetId, ci);
    }

    @Inject(
            method = "bindImageToId([IIII)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bindImageToId(int[] pixels, int width, int height, int targetId, CallbackInfo ci) {
        plugin.bindImageToId(pixels, width, height, targetId, ci);
    }

    @Inject(
            method = "tick()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void tick_redirect(CallbackInfo ci) {
        plugin.tick(ci);
    }

    @Inject(
            method = "getTextureId(Ljava/lang/String;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getTextureId(String par1, CallbackInfoReturnable<Integer> cir) {
        plugin.getTextureId(par1, cir);
    }
}
