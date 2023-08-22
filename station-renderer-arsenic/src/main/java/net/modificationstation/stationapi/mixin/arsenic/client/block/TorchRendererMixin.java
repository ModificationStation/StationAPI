package net.modificationstation.stationapi.mixin.arsenic.client.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class TorchRendererMixin {
    @Inject(
            method = "renderTorchTilted",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 3
            )
    )
    private void stationapi_torch_captureTexture(
            BlockBase block, double e, double f, double g, double h, double par6, CallbackInfo ci,
            @Local(index = 13) int textureId,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(block.getAtlas().getTexture(textureId).getSprite());
    }

    @ModifyVariable(
            method = "renderTorchTilted",
            index = 14,
            at = @At("STORE")
    )
    private int stationapi_torch_modTextureX(
            int value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getX();
    }

    @ModifyVariable(
            method = "renderTorchTilted",
            index = 15,
            at = @At("STORE")
    )
    private int stationapi_torch_modTextureY(
            int value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getY();
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
    private float stationapi_torch_modTextureWidth(
            float constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToWidth(constant, texture.get());
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
    private float stationapi_torch_modTextureHeight(
            float constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToHeight(constant, texture.get());
    }

    @ModifyConstant(
            method = "renderTorchTilted",
            constant = {
                    @Constant(doubleValue = 7D / ATLAS_SIZE),
                    @Constant(doubleValue = 9D / ATLAS_SIZE)
            }
    )
    private double stationapi_torch_modTextureUOffset(
            double constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustU(constant, texture.get());
    }

    @ModifyConstant(
            method = "renderTorchTilted",
            constant = {
                    @Constant(doubleValue = 6D / ATLAS_SIZE),
                    @Constant(doubleValue = 8D / ATLAS_SIZE)
            }
    )
    private double stationapi_torch_modTextureVOffset(
            double constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustV(constant, texture.get());
    }
}
