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
 * instantiation or field write chain with these, and a mod replacing a field later in the constructor wins.
 * The field types stay {@link Map} and {@link Set} for other mods' shadows. The lookups capture the key at
 * {@link Integer#valueOf(int)}, skip the boxing by returning {@code null}, and pass the {@code int} to our map's
 * primitive method. This sees the key as modified by other mods' injectors on {@code ChunkPos.hashCode} and on
 * {@code Integer.valueOf}'s argument. Any other map in the field, including another mod's fastutil map that might
 * override the boxed methods, gets the captured key boxed as in vanilla, which undoes our {@code null} before it
 * reaches that map. Injectors between the two see {@code null} in place of the key: one that expects a non-null key
 * (for example a {@code @ModifyArg} on the map call) breaks, and a key put in its place reaches any other map but
 * not ours.
 */
@Mixin(ChunkCache.class)
class ChunkCacheMixin {
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
