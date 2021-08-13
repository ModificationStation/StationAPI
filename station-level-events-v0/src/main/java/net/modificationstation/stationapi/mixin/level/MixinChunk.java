package net.modificationstation.stationapi.mixin.level;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.BlockRemovedEvent;
import net.modificationstation.stationapi.api.event.level.UnloadChunkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chunk.class)
public class MixinChunk {

    @Shadow public Level level;

    @Inject(method = "method_860", at = @At(target = "Lnet/minecraft/block/BlockBase;onBlockRemoved(Lnet/minecraft/level/Level;III)V", value = "INVOKE", shift = At.Shift.BEFORE))
    private void blockRemoveEvent(int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir) {
        StationAPI.EVENT_BUS.post(new BlockRemovedEvent(level, i, j, k));
    }

    // TODO: CURRENTLY BROKEN CAUSE CHUNKS NEVER UNLOAD ON CLIENT. :NOTCHTHONK:
    @Override
    protected void finalize() throws Throwable {
        StationAPI.EVENT_BUS.post(new UnloadChunkEvent(level, (Chunk) (Object) this));
        super.finalize();
    }
}
