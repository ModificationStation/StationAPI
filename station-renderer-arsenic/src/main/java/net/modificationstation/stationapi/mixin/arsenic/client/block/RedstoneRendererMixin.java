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
public class RedstoneRendererMixin {

    @Unique
    private Sprite
            stationapi_redstone_texture1,
            stationapi_redstone_texture2,
            stationapi_redstone_curTexture;

    @Inject(
            method = "renderRedstoneDust",
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
    private void stationapi_redstone_captureTexture1(
            BlockBase block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            Tessellator var5, int var6, int texture
    ) {
        Atlas atlas = block.getAtlas();
        stationapi_redstone_texture1 = stationapi_redstone_curTexture = atlas.getTexture(texture).getSprite();
        stationapi_redstone_texture2 = atlas.getTexture(texture + 1).getSprite();
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 13,
            at = @At("STORE")
    )
    private int stationapi_redstone_modTexture1X(int value) {
        return stationapi_redstone_texture1.getX();
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 14,
            at = @At("STORE")
    )
    private int stationapi_redstone_modTexture1Y(int value) {
        return stationapi_redstone_texture1.getY();
    }

    @ModifyConstant(
            method = "renderRedstoneDust",
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
                    )
            }
    )
    private float stationapi_redstone_modAtlasWidth(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = "renderRedstoneDust",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_redstone_modTexture1Width(float constant) {
        return adjustToWidth(constant, stationapi_redstone_texture1);
    }

    @ModifyConstant(
            method = "renderRedstoneDust",
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
                    )
            }
    )
    private float stationapi_redstone_modAtlasHeight(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = "renderRedstoneDust",
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_redstone_modTexture1Height(float constant) {
        return adjustToHeight(constant, stationapi_redstone_texture1);
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 13,
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            )
    )
    private int stationapi_redstone_modTexture2X1(int value) {
        stationapi_redstone_curTexture = stationapi_redstone_texture2;
        return stationapi_redstone_texture2.getX();
    }

    @ModifyConstant(
            method = "renderRedstoneDust",
            constant = @Constant(intValue = TEX_SIZE)
    )
    private int stationapi_redstone_removeTexture2Offset(int constant) {
        return 0;
    }

    @ModifyConstant(
            method = "renderRedstoneDust",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 2
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 4
                    )
            }
    )
    private float stationapi_redstone_modTexture2Width(float constant) {
        return adjustToWidth(constant, stationapi_redstone_texture2);
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 14,
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            )
    )
    private int stationapi_redstone_modTexture2Y1(int value) {
        return stationapi_redstone_texture2.getY();
    }

    @ModifyConstant(
            method = "renderRedstoneDust",
            constant = {
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 3
                    ),
                    @Constant(
                            floatValue = ADJUSTED_TEX_SIZE,
                            ordinal = 5
                    )
            }
    )
    private float stationapi_redstone_modTexture2Height(float constant) {
        return adjustToHeight(constant, stationapi_redstone_texture2);
    }

    @ModifyConstant(
            method = "renderRedstoneDust",
            constant = {
                    @Constant(
                            doubleValue = 5D / ATLAS_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            doubleValue = 5D / ATLAS_SIZE,
                            ordinal = 1
                    )
            }
    )
    private double stationapi_redstone_modCurTextureUOffset(double constant) {
        return adjustU(constant, stationapi_redstone_curTexture);
    }

    @ModifyConstant(
            method = "renderRedstoneDust",
            constant = {
                    @Constant(
                            doubleValue = 5D / ATLAS_SIZE,
                            ordinal = 2
                    ),
                    @Constant(
                            doubleValue = 5D / ATLAS_SIZE,
                            ordinal = 3
                    )
            }
    )
    private double stationapi_redstone_modCurTextureVOffset(double constant) {
        return adjustV(constant, stationapi_redstone_curTexture);
    }

    @Redirect(
            method = "renderRedstoneDust",
            slice = @Slice(from = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;colour(FFF)V",
                    ordinal = 1
            )),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;colour(FFF)V"
            )
    )
    private void stationapi_redstone_removeInvisibleTextures1(Tessellator instance, float g, float h, float v) {}

    @Redirect(
            method = "renderRedstoneDust",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 4
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 7
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V"
            )
    )
    private void stationapi_redstone_removeInvisibleTextures2(Tessellator instance, double e, double f, double g, double h, double v) {}

    @Redirect(
            method = "renderRedstoneDust",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 12
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 15
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V"
            )
    )
    private void stationapi_redstone_removeInvisibleTextures3(Tessellator instance, double e, double f, double g, double h, double v) {}

    @Redirect(
            method = "renderRedstoneDust",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 20
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 23
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V"
            )
    )
    private void stationapi_redstone_removeInvisibleTextures4(Tessellator instance, double e, double f, double g, double h, double v) {}

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 13,
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            )
    )
    private int stationapi_redstone_modTexture2X2(int value) {
        stationapi_redstone_curTexture = stationapi_redstone_texture2;
        return stationapi_redstone_texture2.getX();
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 14,
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            )
    )
    private int stationapi_redstone_modTexture2Y2(int value) {
        return stationapi_redstone_texture2.getY();
    }

    @Redirect(
            method = "renderRedstoneDust",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 28
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 31
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V"
            )
    )
    private void stationapi_redstone_removeInvisibleTextures5(Tessellator instance, double e, double f, double g, double h, double v) {}

    @Redirect(
            method = "renderRedstoneDust",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 36
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 39
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V"
            )
    )
    private void stationapi_redstone_removeInvisibleTextures6(Tessellator instance, double e, double f, double g, double h, double v) {}

    @Redirect(
            method = "renderRedstoneDust",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 44
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 47
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V"
            )
    )
    private void stationapi_redstone_removeInvisibleTextures7(Tessellator instance, double e, double f, double g, double h, double v) {}

    @Redirect(
            method = "renderRedstoneDust",
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 52
                    ),
                    to = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V",
                            ordinal = 55
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V"
            )
    )
    private void stationapi_redstone_removeInvisibleTextures8(Tessellator instance, double e, double f, double g, double h, double v) {}

    @Inject(
            method = "renderRedstoneDust",
            at = @At("RETURN")
    )
    private void stationapi_redstone_releaseCaptured(BlockBase i, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir) {
        stationapi_redstone_texture1 = null;
        stationapi_redstone_texture2 = null;
        stationapi_redstone_curTexture = null;
    }
}
