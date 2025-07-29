package net.modificationstation.stationapi.mixin.arsenic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.texture.DynamicTexture;
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
                    target = "Lnet/minecraft/client/texture/TextureManager;addDynamicTexture(Lnet/minecraft/client/render/texture/DynamicTexture;)V"
            )
    )
    private void stationapi_stopVanillaTextureBinders(TextureManager textureManager, DynamicTexture arg) {}
}
