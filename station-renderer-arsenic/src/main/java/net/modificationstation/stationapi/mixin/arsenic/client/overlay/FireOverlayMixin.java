package net.modificationstation.stationapi.mixin.arsenic.client.overlay;

import net.minecraft.block.Block;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(HeldItemRenderer.class)
class FireOverlayMixin {
    @Unique
    private Atlas stationapi_fire_atlas;
    @Unique
    private Sprite stationapi_fire_texture;

    @Inject(
            method = "renderFireOverlay",
            at = @At("HEAD")
    )
    private void stationapi_fire_captureAtlas(float par1, CallbackInfo ci) {
        stationapi_fire_atlas = Block.FIRE.getAtlas();
    }

    @ModifyVariable(
            method = "renderFireOverlay",
            index = 5,
            at = @At("STORE")
    )
    private int stationapi_fire_captureTexture(int value) {
        stationapi_fire_texture = stationapi_fire_atlas.getTexture(value).getSprite();
        return value;
    }

    @ModifyVariable(
            method = "renderFireOverlay",
            index = 6,
            at = @At("STORE")
    )
    private int stationapi_fire_modTextureX(int value) {
        return stationapi_fire_texture.getX();
    }

    @ModifyVariable(
            method = "renderFireOverlay",
            index = 7,
            at = @At("STORE")
    )
    private int stationapi_fire_modTextureY(int value) {
        return stationapi_fire_texture.getY();
    }

    @ModifyConstant(
            method = "renderFireOverlay",
            constant = {
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 1
                    )
            }
    )
    private float stationapi_fire_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderFireOverlay",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_fire_modTextureWidth(float constant) {
        return adjustToWidth(constant, stationapi_fire_texture);
    }

    @ModifyConstant(
            method = "renderFireOverlay",
            constant = {
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 2
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 3
                    )
            }
    )
    private float stationapi_fire_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderFireOverlay",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_fire_modTextureHeight(float constant) {
        return adjustToHeight(constant, stationapi_fire_texture);
    }

    @Inject(
            method = "renderFireOverlay",
            at = @At("RETURN")
    )
    private void stationapi_fire_releaseCaptured(float par1, CallbackInfo ci) {
        stationapi_fire_atlas = null;
        stationapi_fire_texture = null;
    }
}
