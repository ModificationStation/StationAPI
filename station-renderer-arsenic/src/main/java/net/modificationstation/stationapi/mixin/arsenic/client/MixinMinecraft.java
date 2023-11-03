package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.class_336;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Redirect(
            method = "init()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;addTextureBinder(Lnet/minecraft/client/render/TextureBinder;)V"
            )
    )
    private void stopVanillaTextureBinders(TextureManager textureManager, class_336 arg) { }
}
