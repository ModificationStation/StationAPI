package net.modificationstation.stationapi.mixin.arsenic.client.block;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class BedRendererMixin {

    @Unique
    private Atlas stationapi_bed_atlas;
    @Unique
    private Sprite stationapi_bed_texture;

    @Inject(
            method = "renderBed",
            at = @At("HEAD")
    )
    private void stationapi_bed_captureAtlas(BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_bed_atlas = block.getAtlas();
    }

    @Inject(
            method = "renderBed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(Lnet/minecraft/level/BlockView;IIII)I",
                    ordinal = 0,
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_bed_captureTexture1(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int var7, int var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, int texture1
    ) {
        stationapi_bed_texture = stationapi_bed_atlas.getTexture(texture1).getSprite();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderBed",
            index = 27,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_bed_modTexture1X(int x) {
        return stationapi_bed_texture.getX();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderBed",
            index = 28,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_bed_modTexture1Y(int y) {
        return stationapi_bed_texture.getY();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    floatValue = ATLAS_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_bed_modAtlasWidth1(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    intValue = ArsenicBlockRenderer.TEX_SIZE,
                    ordinal = 0
            )
    )
    private int stationapi_bed_modTexture1Width(int constant) {
        return stationapi_bed_texture.getContents().getWidth();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 0
            )
    )
    private double stationapi_bed_modTexture1WidthOffset(double constant) {
        return adjustToWidth(constant, stationapi_bed_texture);
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ATLAS_SIZE,
                    ordinal = 0
            )
    )
    private double stationapi_bed_modAtlasWidth2(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    floatValue = ATLAS_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_bed_modAtlasHeight1(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    intValue = ArsenicBlockRenderer.TEX_SIZE,
                    ordinal = 1
            )
    )
    private int stationapi_bed_modTexture1Height(int constant) {
        return stationapi_bed_texture.getContents().getHeight();
    }


    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 1
            )
    )
    private double stationapi_bed_modTexture1HeightOffset(double constant) {
        return adjustToHeight(constant, stationapi_bed_texture);
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ATLAS_SIZE,
                    ordinal = 1
            )
    )
    private double stationapi_bed_modAtlasHeight2(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @Inject(
            method = "renderBed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(Lnet/minecraft/level/BlockView;IIII)I",
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_bed_captureTexture2(
            BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int var7, int var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21, float var22, float var23, float var24, float var25, float var26, int texture2
    ) {
        stationapi_bed_texture = stationapi_bed_atlas.getTexture(texture2).getSprite();
    }

    @ModifyVariable(
            method = "renderBed",
            index = 28,
            at = @At(
                    value = "STORE",
                    ordinal = 1
            )
    )
    private int stationapi_bed_modTexture2X(int x) {
        return stationapi_bed_texture.getX();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderBed",
            index = 29,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_bed_modTexture2Y(int y) {
        return stationapi_bed_texture.getY();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    floatValue = ATLAS_SIZE,
                    ordinal = 2
            )
    )
    private float stationapi_bed_modAtlasWidth2(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    intValue = ArsenicBlockRenderer.TEX_SIZE,
                    ordinal = 2
            )
    )
    private int stationapi_bed_modTexture2Width(int constant) {
        return stationapi_bed_texture.getContents().getWidth();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 2
            )
    )
    private double stationapi_bed_modTexture2WidthOffset(double constant) {
        return adjustToWidth(constant, stationapi_bed_texture);
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ATLAS_SIZE,
                    ordinal = 2
            )
    )
    private double stationapi_bed_modAtlasWidth3(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    floatValue = ATLAS_SIZE,
                    ordinal = 3
            )
    )
    private float stationapi_bed_modAtlasHeight2(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    intValue = ArsenicBlockRenderer.TEX_SIZE,
                    ordinal = 3
            )
    )
    private int stationapi_bed_modTexture2Height(int constant) {
        return stationapi_bed_texture.getContents().getHeight();
    }


    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 3
            )
    )
    private double stationapi_bed_modTexture2HeightOffset(double constant) {
        return adjustToHeight(constant, stationapi_bed_texture);
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ATLAS_SIZE,
                    ordinal = 3
            )
    )
    private double stationapi_bed_modAtlasHeight4(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @Inject(
            method = "renderBed",
            at = @At("RETURN")
    )
    private void stationapi_bed_releaseCaptured(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_bed_atlas = null;
        stationapi_bed_texture = null;
    }
}
