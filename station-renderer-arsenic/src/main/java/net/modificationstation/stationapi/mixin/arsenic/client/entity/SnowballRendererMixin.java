package net.modificationstation.stationapi.mixin.arsenic.client.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.render.entity.SnowballRenderer;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SnowballRenderer.class)
public class SnowballRendererMixin {
    @ModifyVariable(
            method = "method_1207",
            at = @At("STORE"),
            index = 13
    )
    private float stationapi_snowball_modTextureMinU(
            float value,
            @Local(index = 11) int textureId,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(ItemBase.snowball.getAtlas().getTexture(textureId).getSprite());
        return texture.get().getMinU();
    }

    @ModifyVariable(
            method = "method_1207",
            at = @At("STORE"),
            index = 14
    )
    private float stationapi_snowball_modTextureMaxU(
            float value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getMaxU();
    }

    @ModifyVariable(
            method = "method_1207",
            at = @At("STORE"),
            index = 15
    )
    private float stationapi_snowball_modTextureMinV(
            float value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getMinV();
    }

    @ModifyVariable(
            method = "method_1207",
            at = @At("STORE"),
            index = 16
    )
    private float stationapi_snowball_modTextureMaxV(
            float value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getMaxV();
    }
}
