package net.modificationstation.stationapi.mixin.vanillafix;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkCache;
import net.modificationstation.stationapi.api.vanillafix.world.chunk.StationChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@SuppressWarnings({"unchecked", "AddedMixinMembersNamePattern", "rawtypes"})
@Mixin(ChunkCache.class)
public class ChunkCacheMixin implements StationChunkCache {
    @Shadow
    private World world;

    @Shadow
    private Set chunksToUnload;

    @Shadow
    private List chunks;
    @Unique
    private int nextChunkToUnload;

    @Override
    public void unloadChunk(int chunkX, int chunkZ) {
        Vec3i worldSpawn = this.world.getSpawnPos();
        int distanceToSpawnX = chunkX * 16 + 8 - worldSpawn.x;
        int distanceToSpawnZ = chunkZ * 16 + 8 - worldSpawn.z;
        short spawnChunkRadius = 128;
        
        this.chunksToUnload.add(ChunkPos.hashCode(chunkX, chunkZ));
        System.err.println("Unloading chunk: " + chunkX + ", " + chunkZ);
        
//        if (distanceToSpawnX < -spawnChunkRadius || distanceToSpawnX > spawnChunkRadius || distanceToSpawnZ < -spawnChunkRadius || distanceToSpawnZ > spawnChunkRadius) {
//            this.chunksToUnload.add(ChunkPos.hashCode(chunkX, chunkZ));
//            System.err.println("Unloading chunk: " + chunkX + ", " + chunkZ);
//        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/chunk/ChunkCache;storage:Lnet/minecraft/world/chunk/storage/ChunkStorage;", ordinal = 0))
    public void queueChunksToUnload(CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < 10; i++) {
            if (this.nextChunkToUnload >= this.chunks.size()) {
                this.nextChunkToUnload = 0;
                break;
            }

            
            Chunk chunk = (Chunk) this.chunks.get(this.nextChunkToUnload++);
            PlayerEntity player = this.world.getClosestPlayer((chunk.x << 4) + 8.0D, 64.0D, (chunk.z << 4) + 8.0D, 50.0D);
            if (player == null) {
                this.unloadChunk(chunk.x, chunk.z);
            }
        }
    }
}
