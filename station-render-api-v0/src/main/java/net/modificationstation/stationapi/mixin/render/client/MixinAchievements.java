package net.modificationstation.stationapi.mixin.render.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.Achievements;
import net.minecraft.client.texture.TextureManager;
import net.modificationstation.stationapi.api.client.texture.TextureRegistryOld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Achievements.class)
public class MixinAchievements extends ScreenBase {

    @Redirect(method = "method_1998(IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(I)V", ordinal = 0))
    private void bindTerrainTexture(TextureManager textureManager, int i) {
        TextureRegistryOld.getRegistry(TextureRegistryOld.Vanilla.TERRAIN).bindAtlas(textureManager, 0);
    }
}
