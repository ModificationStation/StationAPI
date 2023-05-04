package net.modificationstation.stationapi.mixin.arsenic.client.block;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class RepeaterRendererMixin {

    @Unique
    private Sprite stationapi_repeater_texture;

    @Inject(
            method = "renderRedstoneRepeater",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(I)I",
                    ordinal = 0,
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_repeater_captureTexture(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            int var5, int var6, int var7, Tessellator var8, float var9, double var10, double var12, double var14, double var16, double var18, int texture
    ) {
        stationapi_repeater_texture = block.getAtlas().getTexture(texture).getSprite();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderRedstoneRepeater",
            index = 21,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_repeater_modTextureX(int x) {
        return stationapi_repeater_texture.getX();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderRedstoneRepeater",
            index = 22,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_repeater_modTextureY(int y) {
        return stationapi_repeater_texture.getY();
    }

    @ModifyConstant(
            method = "renderRedstoneRepeater",
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
    private float stationapi_repeater_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderRedstoneRepeater",
            constant = @Constant(
                    floatValue = ArsenicBlockRenderer.ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_repeater_modTextureWidth(float constant) {
        return adjustToWidth(constant, stationapi_repeater_texture);
    }

    @ModifyConstant(
            method = "renderRedstoneRepeater",
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
    private float stationapi_repeater_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderRedstoneRepeater",
            constant = @Constant(
                    floatValue = ArsenicBlockRenderer.ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_repeater_modTextureHeight(float constant) {
        return adjustToHeight(constant, stationapi_repeater_texture);
    }

    @Inject(
            method = "renderRedstoneRepeater",
            at = @At("RETURN")
    )
    private void stationapi_repeater_releaseCaptured(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_repeater_texture = null;
    }
}
