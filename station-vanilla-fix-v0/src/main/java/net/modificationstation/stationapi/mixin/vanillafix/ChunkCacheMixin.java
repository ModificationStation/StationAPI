package net.modificationstation.stationapi.mixin.vanillafix;

import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkCache;
import net.modificationstation.stationapi.api.vanillafix.world.chunk.StationChunkCache;
import net.modificationstation.stationapi.impl.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(ChunkCache.class)
public class ChunkCacheMixin implements StationChunkCache {
    @Shadow
    private World world;

    @Shadow
    private Set chunksToUnload;

    @Override
    public void unloadChunk(int chunkX, int chunkZ) {
        Vec3i worldSpawn = this.world.getSpawnPos();
        int distanceToSpawnX = chunkX * 16 + 8 - worldSpawn.x;
        int distanceToSpawnZ = chunkZ * 16 + 8 - worldSpawn.z;
        short spawnChunkRadius = 128;
        if (distanceToSpawnX < -spawnChunkRadius || distanceToSpawnX > spawnChunkRadius || distanceToSpawnZ < -spawnChunkRadius || distanceToSpawnZ > spawnChunkRadius) {
            this.chunksToUnload.add(ChunkPos.toLong(chunkX, chunkZ));
        }
    }

    public void queueChunksToUnload() { 
        
    }
}
