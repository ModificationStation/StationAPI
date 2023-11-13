package net.modificationstation.stationapi.mixin.arsenic.client.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.world.BlockStateView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderManager.class)
class FluidRendererMixin {
    @Shadow private BlockView blockView;

    @Inject(
            method = "renderFluid",
            at = @At("HEAD")
    )
    private void stationapi_fluid_init(
            Block block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Share("atlas") LocalRef<Atlas> atlas, @Share("textureScale") LocalIntRef textureScale
    ) {
        atlas.set(block.getAtlas());
        textureScale.set(1);
    }

    @ModifyVariable(
            method = "renderFluid",
            index = 6,
            at = @At("STORE")
    )
    private int stationapi_fluid_modColor(int value, Block block, int x, int y, int z) {
        return (block.id == Block.FLOWING_WATER.id || block.id == Block.WATER.id) &&
                Atlases.getTerrain().getTexture(block.getTexture(0)).getSprite().getContents().getAnimation() != null ?
                StationRenderAPI.getBlockColors().getColor(((BlockStateView) blockView).getBlockState(x, y, z), blockView, new BlockPos(x, y, z), -1) :
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
    private void stationapi_fluid_rescaleTexture(
            Block i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Share("textureScale") LocalIntRef textureScale
    ) {
        textureScale.set(2);
    }

    @Inject(
            method = "renderFluid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(II)I",
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 3
            )
    )
    private void stationapi_fluid_captureTexture1(
            Block block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Local(index = 28) int textureId,
            @Share("atlas") LocalRef<Atlas> atlas, @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(atlas.get().getTexture(textureId).getSprite());
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
    private int stationapi_fluid_modTextureX1(
            int value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getX();
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
    private int stationapi_fluid_modTextureY1(
            int value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getY();
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(
                    doubleValue = TEX_SIZE / 2D,
                    ordinal = 0
            )
    )
    private double stationapi_fluid_modTextureWidth1(
            double constant,
            @Share("textureScale") LocalIntRef textureScale, @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToWidth(constant, texture.get()) / textureScale.get();
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
    private double stationapi_fluid_modTextureHeight1(
            double constant,
            @Share("textureScale") LocalIntRef textureScale, @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToHeight(constant, texture.get()) / textureScale.get();
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
    private int stationapi_fluid_modTextureWidth2(
            int constant,
            @Share("textureScale") LocalIntRef textureScale, @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getContents().getWidth() / textureScale.get();
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
    private int stationapi_fluid_modTextureHeight2(
            int constant,
            @Share("textureScale") LocalIntRef textureScale, @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getContents().getHeight() / textureScale.get();
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
    private float stationapi_fluid_modTextureWidth3(
            float constant,
            @Share("textureScale") LocalIntRef textureScale, @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToWidth(constant, texture.get()) / textureScale.get();
    }

    @Inject(
            method = "renderFluid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getBrightness(Lnet/minecraft/level/BlockView;III)F",
                    ordinal = 0
            )
    )
    private void stationapi_fluid_calculateAtlasSizeIndependentUV(
            Block i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Local(index = 29) float var29, @Local(index = 36) float var36, @Local(index = 37) float var37,
            @Share("textureScale") LocalIntRef textureScale, @Share("texture") LocalRef<Sprite> texture,
            @Share("us") LocalFloatRef us, @Share("uc") LocalFloatRef uc, @Share("vs") LocalFloatRef vs, @Share("vc") LocalFloatRef vc
    ) {
        us.set(var36);
        uc.set(var37);
        final float multiplier = (float) texture.get().getContents().getHeight() / textureScale.get() / 2 / StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
        vs.set(MathHelper.sin(var29) * multiplier);
        vc.set(MathHelper.cos(var29) * multiplier);
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 1
            ),
            index = 37
    )
    private float stationapi_fluid_swapToVC1(
            float value,
            @Share("vc") LocalFloatRef vc
    ) {
        return vc.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 1
            ),
            index = 36
    )
    private float stationapi_fluid_swapToVS1(
            float value,
            @Share("vs") LocalFloatRef vs
    ) {
        return vs.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            ),
            index = 37
    )
    private float stationapi_fluid_swapToUC1(
            float value,
            @Share("uc") LocalFloatRef uc
    ) {
        return uc.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            ),
            index = 36
    )
    private float stationapi_fluid_swapToUS1(
            float value,
            @Share("us") LocalFloatRef us
    ) {
        return us.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 3
            ),
            index = 37
    )
    private float stationapi_fluid_swapToVC2(
            float value,
            @Share("vc") LocalFloatRef vc
    ) {
        return vc.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 3
            ),
            index = 36
    )
    private float stationapi_fluid_swapToVS2(
            float value,
            @Share("vs") LocalFloatRef vs
    ) {
        return vs.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            ),
            index = 37
    )
    private float stationapi_fluid_swapToUC2(
            float value,
            @Share("uc") LocalFloatRef uc
    ) {
        return uc.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            ),
            index = 36
    )
    private float stationapi_fluid_swapToUS2(
            float value,
            @Share("us") LocalFloatRef us
    ) {
        return us.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 5
            ),
            index = 37
    )
    private float stationapi_fluid_swapToVC3(
            float value,
            @Share("vc") LocalFloatRef vc
    ) {
        return vc.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 5
            ),
            index = 36
    )
    private float stationapi_fluid_swapToVS3(
            float value,
            @Share("vs") LocalFloatRef vs
    ) {
        return vs.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 6
            ),
            index = 37
    )
    private float stationapi_fluid_swapToUC3(
            float value,
            @Share("uc") LocalFloatRef uc
    ) {
        return uc.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 6
            ),
            index = 36
    )
    private float stationapi_fluid_swapToUS3(
            float value,
            @Share("us") LocalFloatRef us
    ) {
        return us.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 7
            ),
            index = 37
    )
    private float stationapi_fluid_swapToVC4(
            float value,
            @Share("vc") LocalFloatRef vc
    ) {
        return vc.get();
    }

    @ModifyVariable(
            method = "renderFluid",
            at = @At(
                    value = "LOAD",
                    ordinal = 7
            ),
            index = 36
    )
    private float stationapi_fluid_swapToVS4(
            float value,
            @Share("vs") LocalFloatRef vs
    ) {
        return vs.get();
    }

    @Inject(
            method = "renderFluid",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getTextureForSide(II)I",
                    ordinal = 2,
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private void stationapi_fluid_captureTexture2(
            Block i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Local(index = 32) int textureId,
            @Share("atlas") LocalRef<Atlas> atlas, @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(atlas.get().getTexture(textureId).getSprite());
    }

    @ModifyVariable(
            method = "renderFluid",
            index = 33,
            at = @At("STORE")
    )
    private int stationapi_fluid_modTextureX2(
            int value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getX();
    }

    @ModifyVariable(
            method = "renderFluid",
            index = 34,
            at = @At("STORE")
    )
    private int stationapi_fluid_modTextureY2(
            int value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getY();
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(
                    intValue = TEX_SIZE,
                    ordinal = 3
            )
    )
    private int stationapi_fluid_modTextureWidth3(
            int constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getContents().getWidth() / 2;
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(floatValue = TEX_SIZE)
    )
    private float stationapi_fluid_modTextureHeight3(
            float constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getContents().getHeight() / 2F;
    }

    @ModifyConstant(
            method = "renderFluid",
            constant = @Constant(
                    intValue = TEX_SIZE,
                    ordinal = 4
            )
    )
    private int stationapi_fluid_modTextureHeight4(
            int constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getContents().getHeight() / 2;
    }
}
