package net.modificationstation.stationapi.mixin.arsenic.client.entity;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Unique
    private Atlas stationapi_fire_atlas;
    @Unique
    private Sprite
            stationapi_fire_texture1,
            stationapi_fire_texture2;

    @Inject(
            method = "method_2028",
            at = @At("HEAD")
    )
    private void stationapi_fire_captureAtlas(EntityBase d, double e, double f, double g, float par5, CallbackInfo ci) {
        stationapi_fire_atlas = BlockBase.FIRE.getAtlas();
    }

    @ModifyVariable(
            method = "method_2028",
            index = 9,
            at = @At("STORE")
    )
    private int stationapi_fire_captureTexture(int value) {
        stationapi_fire_texture1 = stationapi_fire_atlas.getTexture(value).getSprite();
        stationapi_fire_texture2 = stationapi_fire_atlas.getTexture(value + 16).getSprite();
        return value;
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At("STORE"),
            index = 10
    )
    private int stationapi_fire_modTextureX1(int value) {
        return stationapi_fire_texture1.getX();
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At("STORE"),
            index = 11
    )
    private int stationapi_fire_modTextureY1(int value) {
        return stationapi_fire_texture1.getY();
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
    private float stationapi_fire_modTexture1Width(float constant) {
        return adjustToWidth(constant, stationapi_fire_texture1);
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
    private float stationapi_fire_modTexture1Height(float constant) {
        return adjustToHeight(constant, stationapi_fire_texture1);
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            ),
            index = 10
    )
    private int stationapi_fire_modTextureX2(int value) {
        return stationapi_fire_texture1.getX();
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            ),
            index = 11
    )
    private int stationapi_fire_modTextureY2(int value) {
        return stationapi_fire_texture1.getY();
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            ),
            index = 10
    )
    private int stationapi_fire_modTextureX3(int value) {
        return stationapi_fire_texture2.getX();
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
    private float stationapi_fire_modTexture2Width(float constant) {
        return adjustToWidth(constant, stationapi_fire_texture2);
    }

    @ModifyVariable(
            method = "method_2028",
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            ),
            index = 11
    )
    private int stationapi_fire_modTextureY3(int value) {
        return stationapi_fire_texture2.getY();
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
    private float stationapi_fire_modTexture2Height(float constant) {
        return adjustToHeight(constant, stationapi_fire_texture2);
    }

    @Inject(
            method = "method_2028",
            at = @At("RETURN")
    )
    private void stationapi_fire_releaseCaptured(EntityBase d, double e, double f, double g, float par5, CallbackInfo ci) {
        stationapi_fire_atlas = null;
        stationapi_fire_texture1 = null;
    }
}
