package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationloader.impl.client.texture.OpenGLHelper;
import net.modificationstation.stationloader.impl.client.texture.TextureRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextureManager.class)
@Environment(EnvType.CLIENT)
public class MixinTextureManager {

    @Redirect(method = "bindTexture(I)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    private void onBindTexture1(int target, int texture) {
        for (TextureRegistry registry : TextureRegistry.registries())
            if (registry.getAtlasTexture((TextureManager) (Object) this, 0) == texture) {
                registry.bindAtlas((TextureManager) (Object) this, 0);
                return;
            }
        OpenGLHelper.bindTexture(target, texture);
    }
}
