package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.class_336;
import net.minecraft.client.Minecraft;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
class MinecraftMixin {
    @Redirect(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/texture/TextureManager;method_1087(Lnet/minecraft/class_336;)V"
            )
    )
    private void stationapi_stopVanillaTextureBinders(TextureManager textureManager, class_336 arg) {}
}
