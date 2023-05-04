package net.modificationstation.stationapi.mixin.arsenic.client.block;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class LadderRendererMixin {

    @Unique
    private Sprite stationapi_ladder_texture;

    @Inject(
            method = "renderLadder",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 3
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_ladder_captureTexture(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int texture
    ) {
        stationapi_ladder_texture = block.getAtlas().getTexture(texture).getSprite();
    }

    @ModifyVariable(
            method = "renderLadder",
            index = 8,
            at = @At("STORE")
    )
    private int stationapi_ladder_modTextureX(int value) {
        return stationapi_ladder_texture.getX();
    }

    @ModifyVariable(
            method = "renderLadder",
            index = 9,
            at = @At("STORE")
    )
    private int stationapi_ladder_modTextureY(int value) {
        return stationapi_ladder_texture.getY();
    }

    @ModifyConstant(
            method = "renderLadder",
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
    private float stationapi_ladder_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderLadder",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_ladder_modTextureWidth(float constant) {
        return adjustToWidth(constant, stationapi_ladder_texture);
    }

    @ModifyConstant(
            method = "renderLadder",
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
    private float stationapi_ladder_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderLadder",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_ladder_modTextureHeight(float constant) {
        return adjustToHeight(constant, stationapi_ladder_texture);
    }

    @Inject(
            method = "renderLadder",
            at = @At("RETURN")
    )
    private void stationapi_ladder_releaseCaptured(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_ladder_texture = null;
    }
}
