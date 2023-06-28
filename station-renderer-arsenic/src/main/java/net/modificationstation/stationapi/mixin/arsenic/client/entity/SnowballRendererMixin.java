package net.modificationstation.stationapi.mixin.arsenic.client.entity;

import net.minecraft.client.render.entity.SnowballRenderer;
import net.minecraft.entity.projectile.Fireball;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowballRenderer.class)
public class SnowballRendererMixin {
    @Unique
    private Atlas stationapi_snowball_atlas;
    @Unique
    private Sprite stationapi_snowball_texture;

    @Inject(
            method = "method_1207",
            at = @At("HEAD")
    )
    private void stationapi_snowball_captureAtlas(Fireball d, double e, double f, double g, float h, float par6, CallbackInfo ci) {
        stationapi_snowball_atlas = ItemBase.snowball.getAtlas();
    }

    @ModifyVariable(
            method = "method_1207",
            at = @At("STORE"),
            index = 11
    )
    private int stationapi_snowball_captureTexture(int value) {
        stationapi_snowball_texture = stationapi_snowball_atlas.getTexture(value).getSprite();
        return value;
    }

    @ModifyVariable(
            method = "method_1207",
            at = @At("STORE"),
            index = 13
    )
    private float stationapi_snowball_modTextureMinU(float value) {
        return stationapi_snowball_texture.getMinU();
    }

    @ModifyVariable(
            method = "method_1207",
            at = @At("STORE"),
            index = 14
    )
    private float stationapi_snowball_modTextureMaxU(float value) {
        return stationapi_snowball_texture.getMaxU();
    }

    @ModifyVariable(
            method = "method_1207",
            at = @At("STORE"),
            index = 15
    )
    private float stationapi_snowball_modTextureMinV(float value) {
        return stationapi_snowball_texture.getMinV();
    }

    @ModifyVariable(
            method = "method_1207",
            at = @At("STORE"),
            index = 16
    )
    private float stationapi_snowball_modTextureMaxV(float value) {
        return stationapi_snowball_texture.getMaxV();
    }

    @Inject(
            method = "method_1207",
            at = @At("RETURN")
    )
    private void stationapi_snowball_releaseCaptured(Fireball d, double e, double f, double g, float h, float par6, CallbackInfo ci) {
        stationapi_snowball_atlas = null;
        stationapi_snowball_texture = null;
    }
}
