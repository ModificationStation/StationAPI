package net.modificationstation.stationapi.mixin.arsenic.client.block;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class FireRendererMixin {

    @Unique
    private Sprite
            stationapi_fire_texture1,
            stationapi_fire_texture2;

    @Inject(
            method = "renderFire",
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
    private void stationapi_fire_captureTexture1(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int texture
    ) {
        Atlas atlas = block.getAtlas();
        stationapi_fire_texture1 = atlas.getTexture(texture).getSprite();
        stationapi_fire_texture2 = atlas.getTexture(texture + 16).getSprite();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderFire",
            index = 8,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_fire_modTexture1X1(int x) {
        return stationapi_fire_texture1.getX();
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @ModifyVariable(
            method = "renderFire",
            index = 9,
            at = @At(
                    value = "STORE",
                    ordinal = 0
            )
    )
    private int stationapi_fire_modTexture1Y1(int y) {
        return stationapi_fire_texture1.getY();
    }

    @ModifyConstant(
            method = "renderFire",
            constant = {
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 1
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 4
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 5
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 8
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 9
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 12
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 13
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 16
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 17
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 20
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 21
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 24
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 25
                    )
            }
    )
    private float stationapi_fire_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderFire",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 4
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 8
                    )
            }
    )
    private float stationapi_fire_modTexture1Width(float constant) {
        return adjustToWidth(constant, stationapi_fire_texture1);
    }

    @ModifyConstant(
            method = "renderFire",
            constant = {
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
                            ordinal = 6
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 7
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 10
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 11
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 14
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 15
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 18
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 19
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 22
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 23
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 26
                    ),
                    @Constant(
                            floatValue = ATLAS_SIZE,
                            ordinal = 27
                    )
            }
    )
    private float stationapi_fire_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderFire",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 1
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 5
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 9
                    )
            }
    )
    private float stationapi_fire_modTexture1Height(float constant) {
        return adjustToHeight(constant, stationapi_fire_texture1);
    }

    @ModifyVariable(
            method = "renderFire",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            )
    )
    private int stationapi_fire_modTexture2X1(int value) {
        return stationapi_fire_texture2.getX();
    }

    @ModifyConstant(
            method = "renderFire",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 2
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 6
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 10
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 12
                    )
            }
    )
    private float stationapi_fire_modTexture2Width(float constant) {
        return adjustToWidth(constant, stationapi_fire_texture2);
    }

    @ModifyVariable(
            method = "renderFire",
            index = 9,
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            )
    )
    private int stationapi_fire_modTexture2Y1(int value) {
        return stationapi_fire_texture2.getY();
    }

    @ModifyConstant(
            method = "renderFire",
            constant = @Constant(intValue = TEX_SIZE)
    )
    private int stationapi_fire_removeTexture2Offset1(int constant) {
        return 0;
    }

    @ModifyConstant(
            method = "renderFire",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 3
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 7
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 11
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 13
                    )
            }
    )
    private float stationapi_fire_modTexture2Height(float constant) {
        return adjustToHeight(constant, stationapi_fire_texture2);
    }

    @ModifyConstant(
            method = "renderFire",
            constant = @Constant(floatValue = TEX_SIZE)
    )
    private float stationapi_first_removeTexture2Offset2(float constant) {
        return 0;
    }

    @ModifyVariable(
            method = "renderFire",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            )
    )
    private int stationapi_fire_modTexture1X2(int value) {
        return stationapi_fire_texture1.getX();
    }

    @ModifyVariable(
            method = "renderFire",
            index = 9,
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            )
    )
    private int stationapi_fire_modTexture1Y2(int value) {
        return stationapi_fire_texture1.getY();
    }

    @ModifyVariable(
            method = "renderFire",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 6
            )
    )
    private int stationapi_fire_modTexture2X2(int value) {
        return stationapi_fire_texture2.getX();
    }

    @ModifyVariable(
            method = "renderFire",
            index = 9,
            at = @At(
                    value = "LOAD",
                    ordinal = 6
            )
    )
    private int stationapi_fire_modTexture2Y2(int value) {
        return stationapi_fire_texture2.getY();
    }

    @ModifyVariable(
            method = "renderFire",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 8
            )
    )
    private int stationapi_fire_modTexture1X3(int value) {
        return stationapi_fire_texture1.getX();
    }

    @ModifyVariable(
            method = "renderFire",
            index = 9,
            at = @At(
                    value = "LOAD",
                    ordinal = 8
            )
    )
    private int stationapi_fire_modTexture1Y3(int value) {
        return stationapi_fire_texture1.getY();
    }

    @ModifyVariable(
            method = "renderFire",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 10
            )
    )
    private int stationapi_fire_modTexture2X3(int value) {
        return stationapi_fire_texture2.getX();
    }

    @ModifyVariable(
            method = "renderFire",
            index = 9,
            at = @At(
                    value = "LOAD",
                    ordinal = 10
            )
    )
    private int stationapi_fire_modTexture2Y3(int value) {
        return stationapi_fire_texture2.getY();
    }

    @ModifyVariable(
            method = "renderFire",
            index = 8,
            at = @At(
                    value = "LOAD",
                    ordinal = 12
            )
    )
    private int stationapi_fire_modTexture2X4(int value) {
        return stationapi_fire_texture2.getX();
    }

    @ModifyVariable(
            method = "renderFire",
            index = 9,
            at = @At(
                    value = "LOAD",
                    ordinal = 12
            )
    )
    private int stationapi_fire_modTexture2Y4(int value) {
        return stationapi_fire_texture2.getY();
    }

    @Inject(
            method = "renderFire",
            at = @At("RETURN")
    )
    private void stationapi_fire_releaseCaptured(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_fire_texture1 = null;
        stationapi_fire_texture2 = null;
    }
}
