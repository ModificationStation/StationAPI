package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.util.maths.Vec2i;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.StationFlatteningLevel;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(Level.class)
public abstract class MixinLevel implements StationFlatteningLevel {
    @Shadow public abstract Chunk getChunk(int x, int z);

    @Shadow @Final public Dimension dimension;

    @Shadow protected abstract void method_235(int i, int j, int k, int l);

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return getChunk(x, z).getBlockState(x & 15, y, z & 15);
    }

    @Override
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return getChunk(x, z).setBlockState(x & 15, y, z & 15, blockState);
    }

    @Override
    public BlockState setBlockStateWithNotify(int x, int y, int z, BlockState blockState) {
        BlockState oldBlockState = setBlockState(x, y, z, blockState);
        if (oldBlockState != null) {
            method_235(x, y, z, blockState.getBlock().id);
            return oldBlockState;
        }
        return null;
    }

    @Inject(
            method = "method_248()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/chunk/Chunk;tiles:[B",
                    args = "array=get"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void captureChunkXYZ(CallbackInfo ci, Iterator<Vec2i> var1, Vec2i var2, int var3, int var4, Chunk var5, int var6, int var7, int var8, int var9, int var10) {
        stationapi$capturedChunk = var5;
        stationapi$capturedX = var8;
        stationapi$capturedY = var10;
        stationapi$capturedZ = var9;
    }

    @Unique
    private Chunk stationapi$capturedChunk;
    @Unique
    private int stationapi$capturedX, stationapi$capturedY, stationapi$capturedZ;

    @Redirect(
            method = "method_248()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/chunk/Chunk;tiles:[B",
                    args = "array=get"
            )
    )
    private byte redirectTilesAccess(byte[] array, int index) {
        return 0;
    }

    @ModifyVariable(
            method = "method_248()V",
            index = 11,
            at = @At(
                    value = "STORE",
                    ordinal = 1
            )
    )
    private int modifyId(int original) {
        BlockBase block = stationapi$capturedChunk.getBlockState(stationapi$capturedX, stationapi$capturedY, stationapi$capturedZ).getBlock();
        return block == null ? 0 : block.id;
    }
    
    @ModifyConstant(method = {
        "getTileId",
        "isTileLoaded",
        "method_155",
        "setTileWithMetadata",
        "setTileInChunk",
        "getTileMeta",
        "method_223",
        "isAboveGround",
        "getLightLevel",
        "placeTile(IIIZ)I",
        "method_164",
        "method_205",
        "method_193",
        "method_248"
    }, constant = @Constant(intValue = 128))
    private int changeMaxHeight(int value) {
        return getTopY();
    }

    @SuppressWarnings("MixinAnnotationTarget")
    @ModifyConstant(method = {
            "getTileId",
            "setTileWithMetadata",
            "setTileInChunk",
            "getTileMeta",
            "method_223",
            "isAboveGround",
            "getLightLevel",
            "placeTile(IIIZ)I",
            "method_164",
            "method_205"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO))
    private int changeBottomYGE(int value) {
        return getBottomY();
    }

    @SuppressWarnings("MixinAnnotationTarget")
    @ModifyConstant(method = {
            "isTileLoaded",
            "method_155",
            "method_164",
            "method_248",
            "method_162"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO))
    private int changeBottomYLT(int value) {
        return getBottomY();
    }

    @SuppressWarnings("MixinAnnotationTarget")
    @ModifyConstant(method = {
            "method_228"
    }, constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_OR_EQUAL_TO_ZERO))
    private int changeBottomYLE(int value) {
        return getBottomY();
    }

    @ModifyConstant(method = {
            "method_193",
            "method_164"
    }, constant = @Constant(intValue = 0, ordinal = 0))
    private int changeBottomY(int value) {
        return getBottomY();
    }
    
    @ModifyConstant(method = {
        "getLightLevel",
        "placeTile(IIIZ)I",
        "method_164",
        "method_228"
    }, constant = @Constant(intValue = 127))
    private int changeMaxBlockHeight(int value) {
        return getTopY() - 1;
    }
    
    @ModifyConstant(method = {
        "method_162"
    }, constant = @Constant(intValue = 200))
    private int changeMaxEntityCalcHeight(int value) {
        return getTopY() + 64;
    }

    @Unique
    @Override
    public int getHeight() {
        return ((StationDimension) this.dimension).getActualLevelHeight();
    }

    @Override
    public int getBottomY() {
        return ((StationDimension) this.dimension).getActualBottomY();
    }
}
