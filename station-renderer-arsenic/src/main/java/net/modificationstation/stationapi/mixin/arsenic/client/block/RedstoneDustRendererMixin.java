package net.modificationstation.stationapi.mixin.arsenic.client.block;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.BlockRenderManager;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderManager.class)
class RedstoneDustRendererMixin {
    @Inject(
            method = "renderRedstoneDust",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/block/BlockRenderManager;textureOverride:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 3
            )
    )
    private void stationapi_redstone_captureTexture1(
            Block block, int j, int k, int par4, CallbackInfoReturnable<Boolean> cir,
            @Local(index = 7) int textureId,
            @Share("curTexture") LocalRef<Sprite> curTexture, @Share("texture1") LocalRef<Sprite> texture1, @Share("texture2") LocalRef<Sprite> texture2
    ) {
        Atlas atlas = block.getAtlas();
        Sprite texture = atlas.getTexture(textureId).getSprite();
        curTexture.set(texture);
        texture1.set(texture);
        texture2.set(atlas.getTexture(textureId + 1).getSprite());
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 13,
            at = @At("STORE")
    )
    private int stationapi_redstone_modTexture1X(
            int value,
            @Share("texture1") LocalRef<Sprite> texture1
    ) {
        return texture1.get().getX();
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 14,
            at = @At("STORE")
    )
    private int stationapi_redstone_modTexture1Y(
            int value,
            @Share("texture1") LocalRef<Sprite> texture1
    ) {
        return texture1.get().getY();
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
    private float stationapi_redstone_modTexture1Width(
            float constant,
            @Share("texture1") LocalRef<Sprite> texture1
    ) {
        return adjustToWidth(constant, texture1.get());
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
    private float stationapi_redstone_modTexture1Height(
            float constant,
            @Share("texture1") LocalRef<Sprite> texture1
    ) {
        return adjustToHeight(constant, texture1.get());
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 13,
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            )
    )
    private int stationapi_redstone_modTexture2X1(
            int value,
            @Share("curTexture") LocalRef<Sprite> curTexture, @Share("texture2") LocalRef<Sprite> texture2
    ) {
        curTexture.set(texture2.get());
        return texture2.get().getX();
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
    private float stationapi_redstone_modTexture2Width(
            float constant,
            @Share("texture2") LocalRef<Sprite> texture2
    ) {
        return adjustToWidth(constant, texture2.get());
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 14,
            at = @At(
                    value = "LOAD",
                    ordinal = 2
            )
    )
    private int stationapi_redstone_modTexture2Y1(
            int value,
            @Share("texture2") LocalRef<Sprite> texture2
    ) {
        return texture2.get().getY();
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
    private float stationapi_redstone_modTexture2Height(
            float constant,
            @Share("texture2") LocalRef<Sprite> texture2
    ) {
        return adjustToHeight(constant, texture2.get());
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
    private double stationapi_redstone_modCurTextureUOffset(
            double constant,
            @Share("curTexture") LocalRef<Sprite> curTexture
    ) {
        return adjustU(constant, curTexture.get());
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
    private double stationapi_redstone_modCurTextureVOffset(
            double constant,
            @Share("curTexture") LocalRef<Sprite> curTexture
    ) {
        return adjustV(constant, curTexture.get());
    }

    @Redirect(
            method = "renderRedstoneDust",
            slice = @Slice(from = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V",
                    ordinal = 1
            )),
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/Tessellator;color(FFF)V"
            )
    )
    private void stationapi_redstone_removeInvisibleTextures1(Tessellator instance, float g, float h, float v) {
    }

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
    private void stationapi_redstone_removeInvisibleTextures2(Tessellator instance, double e, double f, double g, double h, double v) {
    }

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
    private void stationapi_redstone_removeInvisibleTextures3(Tessellator instance, double e, double f, double g, double h, double v) {
    }

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
    private void stationapi_redstone_removeInvisibleTextures4(Tessellator instance, double e, double f, double g, double h, double v) {
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 13,
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            )
    )
    private int stationapi_redstone_modTexture2X2(
            int value,
            @Share("curTexture") LocalRef<Sprite> curTexture, @Share("texture2") LocalRef<Sprite> texture2
    ) {
        curTexture.set(texture2.get());
        return texture2.get().getX();
    }

    @ModifyVariable(
            method = "renderRedstoneDust",
            index = 14,
            at = @At(
                    value = "LOAD",
                    ordinal = 4
            )
    )
    private int stationapi_redstone_modTexture2Y2(
            int value,
            @Share("texture2") LocalRef<Sprite> texture2
    ) {
        return texture2.get().getY();
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
    private void stationapi_redstone_removeInvisibleTextures5(Tessellator instance, double e, double f, double g, double h, double v) {
    }

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
    private void stationapi_redstone_removeInvisibleTextures6(Tessellator instance, double e, double f, double g, double h, double v) {
    }

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
    private void stationapi_redstone_removeInvisibleTextures7(Tessellator instance, double e, double f, double g, double h, double v) {
    }

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
    private void stationapi_redstone_removeInvisibleTextures8(Tessellator instance, double e, double f, double g, double h, double v) {
    }
}
