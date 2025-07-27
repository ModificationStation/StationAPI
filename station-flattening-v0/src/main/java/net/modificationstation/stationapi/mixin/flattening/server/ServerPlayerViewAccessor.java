package net.modificationstation.stationapi.mixin.flattening.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.server.ChunkMap;

@Mixin(ChunkMap.class)
public interface ServerPlayerViewAccessor {
    @Accessor
    List<ChunkMap.TrackedChunk> getChunksToUpdate();
}
