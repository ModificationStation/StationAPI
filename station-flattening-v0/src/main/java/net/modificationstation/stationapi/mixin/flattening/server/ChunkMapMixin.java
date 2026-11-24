package net.modificationstation.stationapi.mixin.flattening.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.ChunkMap;
import net.modificationstation.stationapi.impl.server.network.FlattenedTrackedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkMap.class)
class ChunkMapMixin {
    @WrapOperation(
            method = "getOrCreateChunk",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/server/ChunkMap;II)Lnet/minecraft/server/ChunkMap$TrackedChunk;"
            )
    )
    private ChunkMap.TrackedChunk stationapi_createTrackedChunk(ChunkMap chunkMap, int chunkX, int chunkZ, Operation<ChunkMap.TrackedChunk> original) {
        return new FlattenedTrackedChunk(chunkMap, chunkX, chunkZ);
    }
}
