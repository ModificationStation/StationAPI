package net.modificationstation.stationapi.mixin.arsenic.client.overlay;

import net.minecraft.block.Block;
import net.minecraft.client.gui.hud.InGameHud;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class PortalOverlayMixin {
    @Unique
    private Sprite stationapi_portal_texture;

    @Inject(
            method = "renderPortalOverlay",
            at = @At("HEAD")
    )
    private void stationapi_portal_captureAtlas(float i, int j, int par3, CallbackInfo ci) {
        stationapi_portal_texture = Block.NETHER_PORTAL.getAtlas().getTexture(Block.NETHER_PORTAL.textureId).getSprite();
    }

    @ModifyVariable(
            method = "renderPortalOverlay",
            at = @At("STORE"),
            index = 4
    )
    private float stationapi_portal_modTextureMinU(float value) {
        return stationapi_portal_texture.getMinU();
    }

    @ModifyVariable(
            method = "renderPortalOverlay",
            at = @At("STORE"),
            index = 5
    )
    private float stationapi_portal_modTextureMinV(float value) {
        return stationapi_portal_texture.getMinV();
    }

    @ModifyVariable(
            method = "renderPortalOverlay",
            at = @At("STORE"),
            index = 6
    )
    private float stationapi_portal_modTextureMaxU(float value) {
        return stationapi_portal_texture.getMaxU();
    }

    @ModifyVariable(
            method = "renderPortalOverlay",
            at = @At("STORE"),
            index = 7
    )
    private float stationapi_portal_modTextureMaxV(float value) {
        return stationapi_portal_texture.getMaxV();
    }

    @Inject(
            method = "renderPortalOverlay",
            at = @At("RETURN")
    )
    private void stationapi_portal_releaseCaptured(float i, int j, int par3, CallbackInfo ci) {
        stationapi_portal_texture = null;
    }
}
