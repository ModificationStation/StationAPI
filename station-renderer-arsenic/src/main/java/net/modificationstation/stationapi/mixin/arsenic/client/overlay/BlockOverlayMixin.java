package net.modificationstation.stationapi.mixin.arsenic.client.overlay;

import net.minecraft.block.Block;
import net.minecraft.class_556;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.render.model.BakedModel;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(class_556.class)
class BlockOverlayMixin {
    @Shadow private Minecraft field_2401;
    @Unique
    private Atlas stationapi_block_atlas;
    @Unique
    private Sprite stationapi_block_texture;

    @Inject(
            method = "method_1864",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_556;method_1861(FI)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_overlay_overrideAtlas(
            float par1, CallbackInfo ci, int x, int y, int z, int var5, int blockId
    ) {
        BakedModel model = StationRenderAPI.getBakedModelManager().getBlockModels().getModel(field_2401.world.getBlockState(x, y, z));
        if (model.isBuiltin())
            stationapi_block_atlas = Block.BLOCKS[blockId].getAtlas();
        else
            stationapi_block_texture = model.getSprite();
    }

    @Inject(
            method = "method_1861",
            at = @At("HEAD")
    )
    private void stationapi_block_captureTexture(float i, int texture, CallbackInfo ci) {
        if (stationapi_block_texture == null) {
            if (stationapi_block_atlas == null)
                stationapi_block_atlas = Atlases.getTerrain();
            stationapi_block_texture = stationapi_block_atlas.getTexture(texture).getSprite();
        }
    }

    @ModifyConstant(
            method = "method_1861",
            constant = @Constant(floatValue = 2F / ATLAS_SIZE)
    )
    private float stationapi_block_modTextureCoordOffset(float constant) {
        return adjustU(constant, stationapi_block_texture);
    }

    @ModifyVariable(
            method = "method_1861",
            index = 2,
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            ),
            argsOnly = true
    )
    private int stationapi_block_modTextureX(int value) {
        return stationapi_block_texture.getX();
    }

    @ModifyConstant(
            method = "method_1861",
            constant = {
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 1
                    )
            }
    )
    private int stationapi_block_removeXWrap(int constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "method_1861",
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
    private float stationapi_block_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "method_1861",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_block_modTextureWidth(float constant) {
        return adjustToWidth(constant, stationapi_block_texture);
    }

    @ModifyVariable(
            method = "method_1861",
            index = 2,
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            ),
            argsOnly = true
    )
    private int stationapi_block_modTextureY(int value) {
        return stationapi_block_texture.getY();
    }

    @ModifyConstant(
            method = "method_1861",
            constant = {
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 2
                    ),
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 3
                    )
            }
    )
    private int stationapi_block_removeYWrap(int constant) {
        return 1;
    }

    @ModifyConstant(
            method = "method_1861",
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
    private float stationapi_block_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "method_1861",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_block_modTextureHeight(float constant) {
        return adjustToHeight(constant, stationapi_block_texture);
    }

    @Inject(
            method = "method_1861",
            at = @At("RETURN")
    )
    private void stationapi_block_releaseCaptured(float i, int par2, CallbackInfo ci) {
        stationapi_block_atlas = null;
        stationapi_block_texture = null;
    }
}
