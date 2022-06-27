package net.modificationstation.stationapi.mixin.level;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Level.class)
public abstract class MixinLevel {

    @Shadow public abstract int getTileId(int x, int y, int z);

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;Lnet/minecraft/level/dimension/Dimension;J)V",
                    "<init>(Lnet/minecraft/level/Level;Lnet/minecraft/level/dimension/Dimension;)V",
                    "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/level/dimension/Dimension;)V"
            },
            at = @At("RETURN")
    )
    private void onCor1(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(LevelEvent.Init.builder().level((Level) (Object) this).build());
    }

    @Inject(
            method = "setTileWithMetadata(IIIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;getChunkFromCache(II)Lnet/minecraft/level/chunk/Chunk;",
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void onBlockSetWithMeta(int x, int y, int z, int blockId, int blockMeta, CallbackInfoReturnable<Boolean> cir, Chunk chunk) {
        if (
                StationAPI.EVENT_BUS.post(LevelEvent.BlockSet.builder()
                        .level((Level) (Object) this)
                        .chunk(chunk)
                        .x(x).y(y).z(z)
                        .blockId(blockId).blockMeta(blockMeta)
                        .build()
                ).isCanceled()
        ) cir.setReturnValue(false);
    }

    @Inject(
            method = "setTileInChunk(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;getChunkFromCache(II)Lnet/minecraft/level/chunk/Chunk;",
                    shift = At.Shift.BY,
                    by = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void onBlockSet(int x, int y, int z, int blockId, CallbackInfoReturnable<Boolean> cir, Chunk chunk) {
        if (
                StationAPI.EVENT_BUS.post(LevelEvent.BlockSet.builder()
                        .level((Level) (Object) this)
                        .chunk(chunk)
                        .x(x).y(y).z(z)
                        .blockId(blockId)
                        .build()
                ).isCanceled()
        ) cir.setReturnValue(false);
    }

    @Inject(
            method = "canPlaceTile(IIIIZI)Z",
            at = {
                    @At(
                            value = "RETURN",
                            ordinal = 1
                    ),
                    @At(
                            value = "RETURN",
                            ordinal = 2
                    )
            },
            cancellable = true
    )
    private void isBlockReplaceable(int id, int x, int y, int z, boolean noCollision, int meta, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(StationAPI.EVENT_BUS.post(
                LevelEvent.IsBlockReplaceable.builder()
                        .level((Level) (Object) this)
                        .x(x).y(y).z(z)
                        .block(BlockBase.BY_ID[getTileId(x, y, z)])
                        .replacedBy(BlockBase.BY_ID[id]).replacedByMeta(meta)
                        .replace(cir.getReturnValue())
                        .build()
        ).replace);
    }
}
