package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.Vec2i;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(Level.class)
public abstract class MixinLevel implements BlockStateView {


    @Shadow public abstract Chunk getChunk(int x, int z);

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return ((BlockStateView) getChunk(x, z)).getBlockState(x & 15, y, z & 15);
    }

    @Override
    public BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return ((BlockStateView) getChunk(x, z)).setBlockState(x & 15, y, z & 15, blockState);
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
        BlockBase block = ((BlockStateView) stationapi$capturedChunk).getBlockState(stationapi$capturedX, stationapi$capturedY, stationapi$capturedZ).getBlock();
        return block == null ? 0 : block.id;
    }
}
