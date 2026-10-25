package net.modificationstation.stationapi.mixin.vanillafix.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.impl.vanillafix.util.LastAccessInt2ReferenceOpenHashMap;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.Set;

/**
 * The dedicated server's counterpart of {@code ChunkCacheMixin}, which explains the approach.
 */
@Mixin(ServerChunkCache.class)
class ServerChunkCacheMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/world/chunk/ServerChunkCache;chunksToUnload:Ljava/util/Set;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void stationapi_useIntSet(
            ServerChunkCache instance, Set<Integer> set, Operation<Void> original
    ) {
        original.call(instance, new IntOpenHashSet(set));
    }

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/world/chunk/ServerChunkCache;chunksByPos:Ljava/util/Map;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void stationapi_useLastAccessMap(
            ServerChunkCache instance, Map<Integer, Chunk> map, Operation<Void> original
    ) {
        original.call(instance, new LastAccessInt2ReferenceOpenHashMap<>(map));
    }

    @WrapOperation(
            method = {
                    "isChunkLoaded",
                    "getChunk"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Integer;valueOf(I)Ljava/lang/Integer;"
            )
    )
    private Integer stationapi_captureChunkKey(
            int key, Operation<Integer> original,
            @Share("chunkKey") LocalIntRef chunkKey
    ) {
        chunkKey.set(key);
        return null;
    }

    @WrapOperation(
            method = "isChunkLoaded",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;containsKey(Ljava/lang/Object;)Z"
            )
    )
    private boolean stationapi_containsKeyUnboxed(
            Map<Integer, Chunk> map, Object key, Operation<Boolean> original,
            @Share("chunkKey") LocalIntRef chunkKey
    ) {
        return map instanceof LastAccessInt2ReferenceOpenHashMap<Chunk> lastAccessMap
                ? lastAccessMap.containsKey(chunkKey.get())
                : original.call(map, key == null ? chunkKey.get() : key);
    }

    @WrapOperation(
            method = "getChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"
            )
    )
    private Object stationapi_getUnboxed(
            Map<Integer, Chunk> map, Object key, Operation<Object> original,
            @Share("chunkKey") LocalIntRef chunkKey
    ) {
        return map instanceof LastAccessInt2ReferenceOpenHashMap<Chunk> lastAccessMap
                ? lastAccessMap.get(chunkKey.get())
                : original.call(map, key == null ? chunkKey.get() : key);
    }
}
