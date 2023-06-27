package net.modificationstation.stationapi.mixin.arsenic.client.entity;

import net.minecraft.client.render.entity.ProjectileRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileRenderer.class)
public class ProjectileRendererMixin {
    @Shadow private int field_177;
    @Unique
    private Sprite stationapi_projectile_texture;

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void stationapi_projectile_captureTexture(EntityBase d, double e, double f, double g, float h, float par6, CallbackInfo ci) {
        stationapi_projectile_texture = Atlases.getGuiItems().getTexture(field_177).getSprite();
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 11
    )
    private float stationapi_projectile_modTextureMinU(float value) {
        return stationapi_projectile_texture.getMinU();
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 12
    )
    private float stationapi_projectile_modTextureMaxU(float value) {
        return stationapi_projectile_texture.getMaxU();
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 13
    )
    private float stationapi_projectile_modTextureMinV(float value) {
        return stationapi_projectile_texture.getMinV();
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 14
    )
    private float stationapi_projectile_modTextureMaxV(float value) {
        return stationapi_projectile_texture.getMaxV();
    }

    @Inject(
            method = "render",
            at = @At("RETURN")
    )
    private void stationapi_projectile_releaseCaptured(EntityBase d, double e, double f, double g, float h, float par6, CallbackInfo ci) {
        stationapi_projectile_texture = null;
    }
}
