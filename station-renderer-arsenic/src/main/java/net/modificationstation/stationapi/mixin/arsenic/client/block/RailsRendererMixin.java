package net.modificationstation.stationapi.mixin.arsenic.client.block;

import net.minecraft.block.Rail;
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
public class RailsRendererMixin {

    @Unique
    private Sprite stationapi_rails_texture;

    @Inject(
            method = "renderRails",
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
    private void stationapi_rails_captureTexture(
            Rail block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int texture
    ) {
        stationapi_rails_texture = block.getAtlas().getTexture(texture).getSprite();
    }

    @ModifyVariable(
            method = "renderRails",
            index = 9,
            at = @At("STORE")
    )
    private int stationapi_rails_modTextureX(int value) {
        return stationapi_rails_texture.getX();
    }

    @ModifyVariable(
            method = "renderRails",
            index = 10,
            at = @At("STORE")
    )
    private int stationapi_rails_modTextureY(int value) {
        return stationapi_rails_texture.getY();
    }

    @ModifyConstant(
            method = "renderRails",
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
    private float stationapi_rails_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderRails",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_rails_modTextureWidth(float constant) {
        return adjustToWidth(constant, stationapi_rails_texture);
    }

    @ModifyConstant(
            method = "renderRails",
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
    private float stationapi_rails_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderRails",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_rails_modTextureHeight(float constant) {
        return adjustToHeight(constant, stationapi_rails_texture);
    }

    @Inject(
            method = "renderRails",
            at = @At("RETURN")
    )
    private void stationapi_rails_releaseCaptured(Rail i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_rails_texture = null;
    }
}
