package net.modificationstation.stationapi.mixin.vanillafix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkCache;
import net.modificationstation.stationapi.impl.vanillafix.util.LastAccessInt2ReferenceOpenHashMap;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Speeds up chunk lookups on the singleplayer chunk cache. {@code server.ServerChunkCacheMixin} does the same for
 * the dedicated server.
 *
 * <p>Chunk maps are keyed by {@code ChunkPos.hashCode(x, z)}, which puts X in the high 16 bits and Z in the low 16.
 * Java 8+'s {@link HashMap} only spreads hashes with {@code h ^ (h >>> 16)}, so loaded chunks pile up in a few
 * buckets that turn into trees. fastutil mixes all the bits. On top of that, vanilla boxes the key on every lookup,
 * and the JIT doesn't eliminate the box.
 *
 * <p>The maps are swapped by wrapping the field initializers' writes, so other mods' injectors on the same
 * instantiation or field write chain with these. The field types stay {@link Map} and {@link Set} for other mods'
 * shadows. The chunk map is also kept in a typed field, as SmoothBeta does, and the lookups go straight to it: they
 * capture the key at {@link Integer#valueOf(int)}, skip the boxing by returning {@code null}, and pass the
 * {@code int} to the map's primitive method. This sees the key as modified by other mods' injectors on
 * {@code ChunkPos.hashCode} and on {@code Integer.valueOf}'s argument. Injectors between the two see {@code null} in
 * place of the key, and whatever they put in its place is ignored. The lookups don't see a map another mod puts in
 * the field later; the chunk cache's own writes would go to that one.
 */
@Mixin(ChunkCache.class)
class ChunkCacheMixin {
    /** the map put in {@code chunkByPos}, typed, for the lookups */
    @Unique
    private LastAccessInt2ReferenceOpenHashMap<Chunk> stationapi_chunkMap;

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/chunk/ChunkCache;chunksToUnload:Ljava/util/Set;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void stationapi_useIntSet(
            ChunkCache instance, Set<Integer> set, Operation<Void> original
    ) {
        original.call(instance, new IntOpenHashSet(set));
    }

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/chunk/ChunkCache;chunkByPos:Ljava/util/Map;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void stationapi_useLastAccessMap(
            ChunkCache instance, Map<Integer, Chunk> map, Operation<Void> original
    ) {
        stationapi_chunkMap = new LastAccessInt2ReferenceOpenHashMap<>(map);
        original.call(instance, stationapi_chunkMap);
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
        return stationapi_chunkMap.containsKey(chunkKey.get());
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
        return stationapi_chunkMap.get(chunkKey.get());
    }
}
