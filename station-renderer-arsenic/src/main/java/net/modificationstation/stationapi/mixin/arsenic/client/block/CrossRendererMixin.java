package net.modificationstation.stationapi.mixin.arsenic.client.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.client.render.block.BlockRenderManager;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderManager.class)
class CrossRendererMixin {
    @Inject(
            method = "renderCross(Lnet/minecraft/block/Block;IDDD)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/block/BlockRenderManager;textureOverride:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 3
            )
    )
    private void stationapi_crossed_captureTexture(
            Block block, int d, double e, double f, double par5, CallbackInfo ci,
            @Local(index = 10) int textureId,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        texture.set(block.getAtlas().getTexture(textureId).getSprite());
    }

    @ModifyVariable(
            method = "renderCross(Lnet/minecraft/block/Block;IDDD)V",
            index = 11,
            at = @At("STORE")
    )
    private int stationapi_crossed_modTextureX(
            int value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getX();
    }

    @ModifyVariable(
            method = "renderCross(Lnet/minecraft/block/Block;IDDD)V",
            index = 12,
            at = @At("STORE")
    )
    private int stationapi_crossed_modTextureY(
            int value,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return texture.get().getY();
    }

    @ModifyConstant(
            method = "renderCross(Lnet/minecraft/block/Block;IDDD)V",
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
    private float stationapi_crossed_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderCross(Lnet/minecraft/block/Block;IDDD)V",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_crossed_modTextureWidth(
            float constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToWidth(constant, texture.get());
    }

    @ModifyConstant(
            method = "renderCross(Lnet/minecraft/block/Block;IDDD)V",
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
    private float stationapi_crossed_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderCross(Lnet/minecraft/block/Block;IDDD)V",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_crossed_modTextureHeight(
            float constant,
            @Share("texture") LocalRef<Sprite> texture
    ) {
        return adjustToHeight(constant, texture.get());
    }
}
