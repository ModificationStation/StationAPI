package net.modificationstation.stationapi.mixin.arsenic.client.block;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class PistonHeadRendererMixin {

    // PISTON ROD START

    @Unique
    private Sprite stationapi_pistonRod_texture;

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
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
    private void stationapi_pistonRod_captureTexture(
            double e, double f, double g, double h, double i, double j, float k, double par8, CallbackInfo ci,
            int texture
    ) {
        stationapi_pistonRod_texture = Objects.requireNonNullElseGet(stationapi_pistonHead_atlas, Atlases::getTerrain).getTexture(texture).getSprite();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            index = 17,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_pistonRod_modTextureX(int x) {
        return stationapi_pistonRod_texture.getX();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            index = 18,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_pistonRod_modTextureY(int y) {
        return stationapi_pistonRod_texture.getY();
    }

    @ModifyConstant(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            constant = @Constant(
                    floatValue = ATLAS_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_pistonRod_modAtlasWidth1(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            constant = @Constant(
                    floatValue = ATLAS_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_pistonRod_modAtlasHeight1(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyVariable(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            index = 14,
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            ),
            argsOnly = true
    )
    private double stationapi_pistonRod_modTextureWidth(double value) {
        return adjustToWidth(value, stationapi_pistonRod_texture);
    }

    @ModifyConstant(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 0
            )
    )
    private double stationapi_pistonRod_modTextureWidthOffset(double constant) {
        return adjustToWidth(constant, stationapi_pistonRod_texture);
    }

    @ModifyConstant(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            constant = @Constant(
                    doubleValue = ATLAS_SIZE,
                    ordinal = 0
            )
    )
    private double stationapi_pistonRod_modAtlasWidth2(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            constant = @Constant(floatValue = 4)
    )
    private float stationapi_pistonRod_modTextureHeight(float constant) {
        return adjustToHeight(constant, stationapi_pistonRod_texture);
    }

    @ModifyConstant(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            constant = @Constant(
                    doubleValue = ArsenicBlockRenderer.TEX_SIZE_OFFSET,
                    ordinal = 1
            )
    )
    private double stationapi_pistonRod_modTextureHeightOffset(double constant) {
        return adjustToHeight(constant, stationapi_pistonRod_texture);
    }

    @ModifyConstant(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            constant = @Constant(
                    doubleValue = ATLAS_SIZE,
                    ordinal = 1
            )
    )
    private double stationapi_pistonRod_modAtlasHeight2(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @Inject(
            method = {
                    "method_41",
                    "method_54",
                    "method_60"
            },
            at = @At("RETURN")
    )
    private void stationapi_pistonRod_releaseCaptured(double e, double f, double g, double h, double i, double j, float k, double par8, CallbackInfo ci) {
        stationapi_pistonRod_texture = null;
    }

    // PISTON ROD END



    // PISTON HEAD START

    @Unique
    private Atlas stationapi_pistonHead_atlas;

    @Inject(
            method = "renderPistonHead",
            at = @At("HEAD")
    )
    private void stationapi_pistonHead_captureAtlas(BlockBase block, int j, int k, int bl, boolean par5, CallbackInfoReturnable<Boolean> cir) {
        stationapi_pistonHead_atlas = block.getAtlas();
    }

    @Inject(
            method = "renderPistonHead",
            at = @At("RETURN")
    )
    private void stationapi_pistonHead_releaseCaptured(BlockBase i, int j, int k, int bl, boolean par5, CallbackInfoReturnable<Boolean> cir) {
        stationapi_pistonHead_atlas = null;
    }

    // PISTON HEAD END
}
