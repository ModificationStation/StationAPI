package net.modificationstation.stationapi.mixin.arsenic.client.block;

import net.minecraft.block.BlockBase;
import net.minecraft.client.render.block.BlockRenderer;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.modificationstation.stationapi.impl.client.arsenic.renderer.render.ArsenicBlockRenderer.*;

@Mixin(BlockRenderer.class)
public class BlockRendererMixin {

    @Unique
    private Sprite stationapi_block_texture;

    @Inject(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/render/block/BlockRenderer;textureOverride:I",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 1,
                    shift = At.Shift.BY,
                    by = 3
            )
    )
    private void stationapi_block_captureTexture(BlockBase block, double e, double f, double i, int texture, CallbackInfo ci) {
        stationapi_block_texture = block.getAtlas().getTexture(texture).getSprite();
    }

    @ModifyVariable(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            index = 10,
            at = @At("STORE")
    )
    private int stationapi_block_modTextureX(int value) {
        return stationapi_block_texture.getX();
    }

    @ModifyVariable(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            index = 11,
            at = @At("STORE")
    )
    private int stationapi_block_modTextureY(int value) {
        return stationapi_block_texture.getY();
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = {
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 1
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 4
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 6
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 8
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 10
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 12
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 13
                    )
            }
    )
    private double stationapi_block_modTextureWidth1(double constant) {
        return stationapi_block_texture.getContents().getWidth();
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = {
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 1
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 4
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 6
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 8
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 10
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 12
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 13
                    )
            }
    )
    private double stationapi_block_modAtlasWidth1(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = {
                    @Constant(
                            doubleValue = TEX_SIZE_OFFSET,
                            ordinal = 0
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE_OFFSET,
                            ordinal = 2
                    )
            }
    )
    private double stationapi_block_modTextureWidthOffset(double constant) {
        return adjustToWidth(constant, stationapi_block_texture);
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = {
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 2
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 3
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 5
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 7
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 9
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 11
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 14
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE,
                            ordinal = 15
                    )
            }
    )
    private double stationapi_block_modTextureHeight1(double constant) {
        return stationapi_block_texture.getContents().getHeight();
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = {
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 2
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 3
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 5
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 7
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 9
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 11
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 14
                    ),
                    @Constant(
                            doubleValue = ATLAS_SIZE,
                            ordinal = 15
                    )
            }
    )
    private double stationapi_block_modAtlasHeight1(double constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = {
                    @Constant(
                            doubleValue = TEX_SIZE_OFFSET,
                            ordinal = 1
                    ),
                    @Constant(
                            doubleValue = TEX_SIZE_OFFSET,
                            ordinal = 3
                    )
            }
    )
    private double stationapi_block_modTextureHeightOffset(double constant) {
        return adjustToHeight(constant, stationapi_block_texture);
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
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
    private float stationapi_block_modAtlasWidth2(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getWidth();
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 0
            )
    )
    private float stationapi_block_modTextureWidth2(float constant) {
        return adjustToWidth(constant, stationapi_block_texture);
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
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
    private float stationapi_block_modAtlasHeight2(float constant) {
        return StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getHeight();
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = @Constant(
                    floatValue = ADJUSTED_TEX_SIZE,
                    ordinal = 1
            )
    )
    private float stationapi_block_modTextureHeight2(float constant) {
        return adjustToHeight(constant, stationapi_block_texture);
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = {
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 0
                    ),
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 1
                    ),
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 6
                    ),
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 7
                    )
            }
    )
    private int stationapi_block_modTextureHeight3(int constant) {
        return stationapi_block_texture.getContents().getHeight();
    }

    @ModifyConstant(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            constant = {
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 2
                    ),
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 3
                    ),
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 4
                    ),
                    @Constant(
                            intValue = TEX_SIZE,
                            ordinal = 5
                    )
            }
    )
    private int stationapi_block_modTextureWidth3(int constant) {
        return stationapi_block_texture.getContents().getWidth();
    }

    @Inject(
            method = {
                    "renderBottomFace",
                    "renderTopFace",
                    "renderEastFace",
                    "renderWestFace",
                    "renderNorthFace",
                    "renderSouthFace"
            },
            at = @At("RETURN")
    )
    private void stationapi_block_releaseCaptured(BlockBase d, double e, double f, double i, int par5, CallbackInfo ci) {
        stationapi_block_texture = null;
    }

    @Unique
    private final ArsenicBlockRenderer arsenic_plugin = new ArsenicBlockRenderer((BlockRenderer) (Object) this);

    @Inject(
            method = "render(Lnet/minecraft/block/BlockBase;III)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRenderInWorld(BlockBase block, int blockX, int blockY, int blockZ, CallbackInfoReturnable<Boolean> cir) {
        arsenic_plugin.renderWorld(block, blockX, blockY, blockZ, cir);
    }

    @Inject(
            method = "method_48(Lnet/minecraft/block/BlockBase;IF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getRenderType()I"
            ),
            cancellable = true
    )
    private void onRenderInInventory(BlockBase arg, int meta, float brightness, CallbackInfo ci) {
        arsenic_plugin.renderInventory(arg, meta, brightness, ci);
    }
}
