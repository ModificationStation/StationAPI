package net.modificationstation.stationapi.mixin.arsenic.client.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.world.BlockStateView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class FluidRendererMixin {

    @Shadow private BlockView blockView;

    @Unique
    private Atlas stationapi_fluid_atlas;
    @Unique
    private int stationapi_fluid_textureScale;
    @Unique
    private Sprite stationapi_fluid_texture;

    @Inject(
            method = "renderFluid",
            at = @At("HEAD")
    )
    private void stationapi_fluid_init(BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_fluid_atlas = block.getAtlas();
        stationapi_fluid_textureScale = 1;
    }

    @ModifyVariable(
            method = "renderFluid",
            index = 6,
            at = @At("STORE")
    )
    private int stationapi_fluid_modColor(int value, BlockBase block, int x, int y, int z) {
        return (block.id == BlockBase.FLOWING_WATER.id || block.id == BlockBase.STILL_WATER.id) &&
                Atlases.getTerrain().getTexture(block.getTextureForSide(0)).getSprite().getContents().getAnimation() != null ?
                StationRenderAPI.getBlockColors().getColor(((BlockStateView) blockView).getBlockState(x, y, z), blockView, new TilePos(x, y, z), -1) :
                value;
    }

    @Inject(
            method = "renderFluid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(II)I",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_fluid_rescaleTexture(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_fluid_textureScale = 2;
    }

    @Inject(
            method = "renderFluid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(II)I",
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 3
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_fluid_captureTexture1(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, float var7, float var8, float var9, int var10, int var11, boolean[] var12, int var13, float var14, float var15, float var16, float var17, double var18, double var20, Material var22, int var23, float var24, float var25, float var26, float var27, int texture
    ) {
        stationapi_fluid_texture = stationapi_fluid_atlas.getTexture(texture).getSprite();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderFluid",
            index = 30,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_fluid_modTextureX1(int value) {
        return stationapi_fluid_texture.getX();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderFluid",
            index = 31,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_fluid_modTextureY1(int value) {
        return stationapi_fluid_texture.getY();
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(
                    doubleValue = TEX_SIZE / 2D,
                    ordinal = 0
            )
    )
    private double stationapi_fluid_modTextureWidth1(double constant) {
        return adjustToWidth(constant, stationapi_fluid_texture) / stationapi_fluid_textureScale;
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = {
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 2
                    )
            }
    )
    private double stationapi_fluid_modAtlasWidth1(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(
                    doubleValue = TEX_SIZE / 2D,
                    ordinal = 1
            )
    )
    private double stationapi_fluid_modTextureHeight1(double constant) {
        return adjustToHeight(constant, stationapi_fluid_texture) / stationapi_fluid_textureScale;
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = {
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 1
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 3
                    )
            }
    )
    private double stationapi_fluid_modAtlasHeight1(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(
                    intValue = TEX_SIZE,
                    ordinal = 1
            )
    )
    private int stationapi_fluid_modTextureWidth2(int constant) {
        return stationapi_fluid_texture.getContents().getWidth() / stationapi_fluid_textureScale;
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = {
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 0
                    ),
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
                            ordinal = 4
                    )
            }
    )
    private float stationapi_fluid_modAtlasWidth2(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(
                    intValue = TEX_SIZE,
                    ordinal = 2
            )
    )
    private int stationapi_fluid_modTextureHeight2(int constant) {
        return stationapi_fluid_texture.getContents().getHeight() / stationapi_fluid_textureScale;
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = {
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 1
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 5
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 6
                    )
            }
    )
    private float stationapi_fluid_modAtlasHeight2(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(floatValue = TEX_SIZE / 2F)
    )
    private float stationapi_fluid_modTextureWidth3(float constant) {
        return adjustToWidth(constant, stationapi_fluid_texture) / stationapi_fluid_textureScale;
    }

    @Inject(
            method = "renderFluid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(II)I",
                    ordinal = 2,
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_fluid_captureTexture2(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, float var7, float var8, float var9, int var10, int var11, boolean[] var12, int var13, float var14, float var15, float var16, float var17, double var18, double var20, Material var22, int var23, float var24, float var25, float var26, float var27, int var28, int var29, int var30, int var31, int texture
    ) {
        stationapi_fluid_texture = stationapi_fluid_atlas.getTexture(texture).getSprite();
    }

    @ModifyVariable(
            method = "renderFluid",
            index = 33,
            at = @At("STORE")
    )
    private int stationapi_fluid_modTextureX2(int value) {
        return stationapi_fluid_texture.getX();
    }

    @ModifyVariable(
            method = "renderFluid",
            index = 34,
            at = @At("STORE")
    )
    private int stationapi_fluid_modTextureY2(int value) {
        return stationapi_fluid_texture.getY();
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(
                    intValue = TEX_SIZE,
                    ordinal = 3
            )
    )
    private int stationapi_fluid_modTextureWidth3(int constant) {
        return stationapi_fluid_texture.getContents().getWidth() / 2;
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(floatValue = TEX_SIZE)
    )
    private float stationapi_fluid_modTextureHeight3(float constant) {
        return stationapi_fluid_texture.getContents().getHeight() / 2F;
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(
                    intValue = TEX_SIZE,
                    ordinal = 4
            )
    )
    private int stationapi_fluid_modTextureHeight4(int constant) {
        return stationapi_fluid_texture.getContents().getHeight() / 2;
    }

    @Inject(
            method = "renderFluid",
            at = @At("RETURN")
    )
    private void stationapi_fluid_releaseCaptured(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_fluid_atlas = null;
        stationapi_fluid_texture = null;
    }
}
