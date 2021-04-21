package net.modificationstation.stationapi.mixin.sortme.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.TexturePack;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.TextureFactory;
import net.modificationstation.stationapi.api.client.texture.TextureRegistry;
import net.modificationstation.stationapi.impl.client.texture.OpenGLHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextureManager.class)
@Environment(EnvType.CLIENT)
public class MixinTextureManager {

    @Redirect(method = "bindTexture(I)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", remap = false))
    private void onBindTexture1(int target, int texture) {
        for (TextureRegistry registry : TextureRegistry.registries())
            if (registry.getAtlasTexture((TextureManager) (Object) this, 0) == texture) {
                registry.bindAtlas((TextureManager) (Object) this, 0);
                return;
            }
        OpenGLHelper.bindTexture(target, texture);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @Redirect(method = "method_1096()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/TexturePack;getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;"))
    private InputStream fixFakeAtlases(TexturePack texturePack, String string) {
        return texturePack.getResourceAsStream(TextureFactory.INSTANCE.getFakedAtlases().getOrDefault(string, string));
    }
}
