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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class TorchRendererMixin {

    @Unique
    private Sprite stationapi_torch_texture;

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(
            method = "renderTorchTilted",
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
    private void stationapi_torch_captureTexture(
            BlockBase block, double e, double f, double g, double h, double par6, CallbackInfo ci,
            Tessellator var12, int texture
    ) {
        stationapi_torch_texture = block.getAtlas().getTexture(texture).getSprite();
    }

    @ModifyVariable(
            method = "renderTorchTilted",
            index = 14,
            at = @At("STORE")
    )
    private int stationapi_torch_modTextureX(int value) {
        return stationapi_torch_texture.getX();
    }

    @ModifyVariable(
            method = "renderTorchTilted",
            index = 15,
            at = @At("STORE")
    )
    private int stationapi_torch_modTextureY(int value) {
        return stationapi_torch_texture.getY();
    }

    @ModifyConstant(
            method = "renderTorchTilted",
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
    private float stationapi_torch_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderTorchTilted",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_torch_modTextureWidth(float constant) {
        return adjustToWidth(constant, stationapi_torch_texture);
    }

    @ModifyConstant(
            method = "renderTorchTilted",
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
    private float stationapi_torch_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderTorchTilted",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_torch_modTextureHeight(float constant) {
        return adjustToHeight(constant, stationapi_torch_texture);
    }

    @ModifyConstant(
            method = "renderTorchTilted",
            constant = {
                    @Constant(doubleValue = 7D / ATLAS_SIZE),
                    @Constant(doubleValue = 9D / ATLAS_SIZE)
            }
    )
    private double stationapi_torch_modTextureUOffset(double constant) {
        return adjustU(constant, stationapi_torch_texture);
    }

    @ModifyConstant(
            method = "renderTorchTilted",
            constant = {
                    @Constant(doubleValue = 6D / ATLAS_SIZE),
                    @Constant(doubleValue = 8D / ATLAS_SIZE)
            }
    )
    private double stationapi_torch_modTextureVOffset(double constant) {
        return adjustV(constant, stationapi_torch_texture);
    }

    @Inject(
            method = "renderTorchTilted",
            at = @At("RETURN")
    )
    private void stationapi_torch_releaseCaptured(BlockBase d, double e, double f, double g, double h, double par6, CallbackInfo ci) {
        stationapi_torch_texture = null;
    }
}
