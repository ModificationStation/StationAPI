package net.modificationstation.stationapi.mixin.arsenic.client.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.client.render.entity.EntityRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @ModifyVariable(
            method = "method_2028",
            at = @At("STORE"),
            index = 10
    )
    private int stationapi_fire_modTextureX1(
            int value,
            @Local(index = 9) int textureId,
            @Share("texture1") LocalRef<Sprite> texture1, @Share("texture2") LocalRef<Sprite> texture2
    ) {
        Atlas atlas = Block.FIRE.getAtlas();
        texture1.set(atlas.getTexture(textureId).getSprite());
        texture2.set(atlas.getTexture(textureId + 16).getSprite());
        return texture1.get().getX();
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At("STORE"),
            index = 11
    )
    private int stationapi_fire_modTextureY1(
            int value,
            @Share("texture1") LocalRef<Sprite> texture1
    ) {
        return texture1.get().getY();
    }

    @ModifyConstant(
            method = "method_2028",
            constant = {
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 1
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 4
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 5
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 8
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 9
                    )
            }
    )
    private float stationapi_fire_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "method_2028",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 2
                    )
            }
    )
    private float stationapi_fire_modTexture1Width(
            float constant,
            @Share("texture1") LocalRef<Sprite> texture1
    ) {
        return adjustToWidth(constant, texture1.get());
    }

    @ModifyConstant(
            method = "method_2028",
            constant = {
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 2
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 3
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 6
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 7
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 10
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 11
                    )
            }
    )
    private float stationapi_fire_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "method_2028",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 1
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 3
                    )
            }
    )
    private float stationapi_fire_modTexture1Height(
            float constant,
            @Share("texture1") LocalRef<Sprite> texture1
    ) {
        return adjustToHeight(constant, texture1.get());
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            ),
            index = 10
    )
    private int stationapi_fire_modTextureX2(
            int value,
            @Share("texture1") LocalRef<Sprite> texture1
    ) {
        return texture1.get().getX();
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            ),
            index = 11
    )
    private int stationapi_fire_modTextureY2(
            int value,
            @Share("texture1") LocalRef<Sprite> texture1
    ) {
        return texture1.get().getY();
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            ),
            index = 10
    )
    private int stationapi_fire_modTextureX3(
            int value,
            @Share("texture2") LocalRef<Sprite> texture2
    ) {
        return texture2.get().getX();
    }

    @ModifyConstant(
            method = "method_2028",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 4
                    )
            }
    )
    private float stationapi_fire_modTexture2Width(
            float constant,
            @Share("texture2") LocalRef<Sprite> texture2
    ) {
        return adjustToWidth(constant, texture2.get());
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            ),
            index = 11
    )
    private int stationapi_fire_modTextureY3(
            int value,
            @Share("texture2") LocalRef<Sprite> texture2
    ) {
        return texture2.get().getY();
    }

    @ModifyConstant(
            method = "method_2028",
            constant = @Constant(intValue = TEX_SIZE)
    )
    private int stationapi_fire_removeTexture2Offset1(int constant) {
        return 0;
    }

    @ModifyConstant(
            method = "method_2028",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 5
                    )
            }
    )
    private float stationapi_fire_modTexture2Height(
            float constant,
            @Share("texture2") LocalRef<Sprite> texture2
    ) {
        return adjustToHeight(constant, texture2.get());
    }
}
