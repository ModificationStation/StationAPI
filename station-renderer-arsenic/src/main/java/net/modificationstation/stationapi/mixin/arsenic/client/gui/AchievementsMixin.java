package net.modificationstation.stationapi.mixin.arsenic.client.gui;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.Achievements;
import net.minecraft.client.render.Tessellator;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Achievements.class)
public class AchievementsMixin extends ScreenBase {
    @Redirect(
            method = "method_1998",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/menu/Achievements;blit(IIIIII)V",
                    ordinal = 0
            )
    )
    private void stationapi_background_renderTexture(
            Achievements instance, int x, int y, int texX, int texY, int width, int height,
            @Local(index = 26) int textureId
    ) {
        Sprite texture = Atlases.getTerrain().getTexture(textureId).getSprite();
        Tessellator tessellator = Tessellator.INSTANCE;
        tessellator.start();
        tessellator.vertex(x, y + height, zOffset, texture.getMinU(), texture.getMaxV());
        tessellator.vertex(x + width, y + height, zOffset, texture.getMaxU(), texture.getMaxV());
        tessellator.vertex(x + width, y, zOffset, texture.getMaxU(), texture.getMinV());
        tessellator.vertex(x, y, zOffset, texture.getMinU(), texture.getMinV());
        tessellator.draw();
    }
}
