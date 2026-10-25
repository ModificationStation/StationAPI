package net.modificationstation.stationapi.mixin.vanillafix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkCache;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.storage.ChunkStorage;
import net.modificationstation.stationapi.api.vanillafix.world.chunk.StationChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "AddedMixinMembersNamePattern", "rawtypes"})
@Mixin(ChunkCache.class)
public abstract class ChunkCacheMixin implements StationChunkCache {
    @Shadow
    private World world;

    @Shadow
    private Set chunksToUnload;

    @Shadow
    private List chunks;

    @Shadow
    private Map chunkByPos;

    @Shadow
    public abstract Chunk loadChunk(int chunkX, int chunkZ);

    @Unique
    private int nextChunkToUnload;
    
    @Unique
    private Int2ObjectMap<Chunk> stationapi$chunksByPos;

    // Boxing Elimination
    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void getMap(World world, ChunkStorage storage, ChunkSource generator, CallbackInfo ci) {
        stationapi$chunksByPos = new Int2ObjectOpenHashMap<>(1024);
        this.chunkByPos = stationapi$chunksByPos;
    }

    /**
     * @reason Redirecting {@code serverChunkCache.containsKey(Vec2i.hash(chunkX, chunkZ))} still boxes the integer, adding unnecessary memory usage.
     * @author mine_diver
     */
    @Overwrite
    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        return stationapi$chunksByPos.containsKey(ChunkPos.hashCode(chunkX, chunkZ));
    }

    /**
     * @reason This is the only way to avoid integer boxing here.
     * @author mine_diver
     */
    @Overwrite
    public Chunk getChunk(int chunkX, int chunkZ) {
        Chunk var3 = stationapi$chunksByPos.get(ChunkPos.hashCode(chunkX, chunkZ));
        return var3 == null ? loadChunk(chunkX, chunkZ) : var3;
    }
    
    // Chunk Dropping
    @Override
    public void unloadChunk(int chunkX, int chunkZ) {
        Vec3i worldSpawn = this.world.getSpawnPos();
        int distanceToSpawnX = chunkX * 16 + 8 - worldSpawn.x;
        int distanceToSpawnZ = chunkZ * 16 + 8 - worldSpawn.z;
        short spawnChunkRadius = 128;

        if (distanceToSpawnX < -spawnChunkRadius || distanceToSpawnX > spawnChunkRadius || distanceToSpawnZ < -spawnChunkRadius || distanceToSpawnZ > spawnChunkRadius) {
            this.chunksToUnload.add(ChunkPos.hashCode(chunkX, chunkZ));
            //System.err.println("Unloading chunk: " + chunkX + ", " + chunkZ);
        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/chunk/ChunkCache;storage:Lnet/minecraft/world/chunk/storage/ChunkStorage;", ordinal = 0))
    public void queueChunksToUnload(CallbackInfoReturnable<Boolean> cir) {
        for (int i = 0; i < 10; i++) {
            if (this.nextChunkToUnload >= this.chunks.size()) {
                this.nextChunkToUnload = 0;
                break;
            }


            Chunk chunk = (Chunk) this.chunks.get(this.nextChunkToUnload++);
            PlayerEntity player = this.world.getClosestPlayer((chunk.x << 4) + 8.0D, 64.0D, (chunk.z << 4) + 8.0D, 288.0);
            if (player == null) {
                this.unloadChunk(chunk.x, chunk.z);
            }
        }
    }
    
    @ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "intValue=100"))
    public int thanksNotch(int original) {
        return this.chunksToUnload.size();
    }
}
