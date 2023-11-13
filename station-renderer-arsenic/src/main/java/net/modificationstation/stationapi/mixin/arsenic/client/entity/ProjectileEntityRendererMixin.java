package net.modificationstation.stationapi.mixin.arsenic.client.entity;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntityRenderer.class)
class ProjectileEntityRendererMixin {
    @Shadow
    private int itemTextureId;

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void stationapi_projectile_captureTexture(
            Entity d, double e, double f, double g, float h, float par6, CallbackInfo ci,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(Atlases.getGuiItems().getTexture(itemTextureId).getSprite());
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 11
    )
    private float stationapi_projectile_modTextureMinU(
            float value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getMinU();
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 12
    )
    private float stationapi_projectile_modTextureMaxU(
            float value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getMaxU();
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 13
    )
    private float stationapi_projectile_modTextureMinV(
            float value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getMinV();
    }

    @ModifyVariable(
            method = "render",
            at = @At("STORE"),
            index = 14
    )
    private float stationapi_projectile_modTextureMaxV(
            float value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getMaxV();
    }
}
