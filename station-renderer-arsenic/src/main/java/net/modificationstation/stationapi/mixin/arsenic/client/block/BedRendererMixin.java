package net.modificationstation.stationapi.mixin.arsenic.client.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderManager.class)
class BedRendererMixin {
    @Inject(
            method = "renderBed",
            at = @At("HEAD")
    )
    private void stationapi_bed_captureAtlas(
            Block block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Share("atlas") LocalRef<Atlas> atlas
    ) {
        atlas.set(block.getAtlas());
    }

    @Inject(
            method = "renderBed",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getTextureId(Lnet/minecraft/world/BlockView;IIII)I",
                    ordinal = 0,
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private void stationapi_bed_captureTexture1(
            Block i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Local(index = 26) int texture1,
            @Share("atlas") LocalRef<Atlas> atlas, @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(atlas.get().getTexture(texture1).getSprite());
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
    private int stationapi_bed_modTexture1X(
            int x,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getX();
    }

    @ModifyVariable(
            method = "renderBed",
            index = 28,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_bed_modTexture1Y(
            int y,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getY();
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
    private int stationapi_bed_modTexture1Width(
            int constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getContents().getWidth();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 0
            )
    )
    private double stationapi_bed_modTexture1WidthOffset(
            double constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToWidth(constant, texture.get());
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
    private int stationapi_bed_modTexture1Height(
            int constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getContents().getHeight();
    }


    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 1
            )
    )
    private double stationapi_bed_modTexture1HeightOffset(
            double constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToHeight(constant, texture.get());
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
                    target = "Lnet/minecraft/block/Block;getTextureId(Lnet/minecraft/world/BlockView;IIII)I",
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 2
            )
    )
    private void stationapi_bed_captureTexture2(
            Block i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Local(index = 27) int texture2,
            @Share("atlas") LocalRef<Atlas> atlas, @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(atlas.get().getTexture(texture2).getSprite());
    }

    @ModifyVariable(
            method = "renderBed",
            index = 28,
            at = @At(
                    value = "STORE",
                    ordinal = 1
            )
    )
    private int stationapi_bed_modTexture2X(
            int x,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getX();
    }

    @ModifyVariable(
            method = "renderBed",
            index = 29,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_bed_modTexture2Y(
            int y,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getY();
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
    private int stationapi_bed_modTexture2Width(
            int constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getContents().getWidth();
    }

    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 2
            )
    )
    private double stationapi_bed_modTexture2WidthOffset(
            double constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToWidth(constant, texture.get());
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
    private int stationapi_bed_modTexture2Height(
            int constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getContents().getHeight();
    }


    @ModifyConstant(
            method = "renderBed",
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 3
            )
    )
    private double stationapi_bed_modTexture2HeightOffset(
            double constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToHeight(constant, texture.get());
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
}
