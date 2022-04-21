package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureBinder;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(
            method = "init()V",
            at = @At("HEAD")
    )
    private void initRenderSystem(CallbackInfo ci) {
        RenderSystem.initRenderThread();
        RenderSystem.beginInitialization();
        RenderSystem.finishInitialization();
    }

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;addTextureBinder(Lnet/minecraft/client/render/TextureBinder;)V"
            )
    )
    private void stopVanillaTextureBinders(TextureManager textureManager, TextureBinder arg) { }
}
