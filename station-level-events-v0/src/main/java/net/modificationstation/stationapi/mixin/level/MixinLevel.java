package net.modificationstation.stationapi.mixin.level;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Level.class)
public class MixinLevel {

    @Inject(
            method = {
                    "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;Lnet/minecraft/level/dimension/Dimension;J)V",
                    "<init>(Lnet/minecraft/level/Level;Lnet/minecraft/level/dimension/Dimension;)V",
                    "<init>(Lnet/minecraft/level/dimension/DimensionData;Ljava/lang/String;JLnet/minecraft/level/dimension/Dimension;)V"
            },
            at = @At("RETURN")
    )
    private void onCor1(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new LevelEvent.Init((Level) (Object) this));
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
        if (StationAPI.EVENT_BUS.post(new LevelEvent.BlockSet((Level) (Object) this, chunk, x, y, z, blockId, blockMeta)).isCancelled())
            cir.setReturnValue(false);
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
        if (StationAPI.EVENT_BUS.post(new LevelEvent.BlockSet((Level) (Object) this, chunk, x, y, z, blockId)).isCancelled())
            cir.setReturnValue(false);
    }
}
