package net.modificationstation.stationapi.mixin.arsenic.client.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class LeverRendererMixin {
    @Inject(
            method = "renderLever",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 2,
                    shift = At.Shift.BY,
                    by = 3
            )
    )
    private void stationapi_lever_captureTexture(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Local(index = 14) int textureId,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(block.getAtlas().getTexture(textureId).getSprite());
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderLever",
            index = 15,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_lever_modTextureX(
            int x,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getX();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderLever",
            index = 16,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_lever_modTextureY(
            int y,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getY();
    }

    @ModifyConstant(
            method = "renderLever",
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
    private float stationapi_lever_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderLever",
            constant = @Constant(
                    floatValue = ArsenicBlockRenderer.ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_lever_modTextureWidth1(
            float constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToWidth(constant, texture.get());
    }

    @ModifyConstant(
            method = "renderLever",
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
    private float stationapi_lever_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderLever",
            constant = @Constant(
                    floatValue = ArsenicBlockRenderer.ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_lever_modTextureHeight1(
            float constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToHeight(constant, texture.get());
    }

    @ModifyConstant(
            method = "renderLever",
            constant = {
                    @Constant(
                            intValue = 7,
                            ordinal = 2
                    ),
                    @Constant(
                            intValue = 9,
                            ordinal = 0
                    ),
                    @Constant(
                            intValue = 7,
                            ordinal = 3
                    ),
                    @Constant(
                            intValue = 9,
                            ordinal = 1
                    )
            }
    )
    private int stationapi_lever_modTextureWidth2(
            int constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToWidth(constant, texture.get());
    }

    @ModifyConstant(
            method = "renderLever",
            constant = {
                    @Constant(
                            floatValue = TEX_SIZE_OFFSET_F,
                            ordinal = 0
                    ),
                    @Constant(
                            floatValue = TEX_SIZE_OFFSET_F,
                            ordinal = 2
                    )
            }
    )
    private float stationapi_lever_modTextureWidthOffset(
            float constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToWidth(constant, texture.get());
    }

    @ModifyConstant(
            method = "renderLever",
            constant = {
                    @Constant(
                            intValue = 6,
                            ordinal = 4
                    ),
                    @Constant(
                            intValue = 8,
                            ordinal = 3
                    ),
                    @Constant(
                            intValue = 6,
                            ordinal = 5
                    ),
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 0
                    )
            }
    )
    private int stationapi_lever_modTextureHeight2(
            int constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToHeight(constant, texture.get());
    }

    @ModifyConstant(
            method = "renderLever",
            constant = {
                    @Constant(
                            floatValue = TEX_SIZE_OFFSET_F,
                            ordinal = 1
                    ),
                    @Constant(
                            floatValue = TEX_SIZE_OFFSET_F,
                            ordinal = 3
                    )
            }
    )
    private float stationapi_lever_modTextureHeightOffset(
            float constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToHeight(constant, texture.get());
    }
}
