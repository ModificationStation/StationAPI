package net.modificationstation.stationapi.mixin.vanillafix.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.objects.Object2ReferenceOpenHashMap;
import net.minecraft.client.world.chunk.MultiplayerChunkCache;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link ChunkPos#hashCode()} has the same poor spread in {@link HashMap} as the {@code Integer} keys in
 * {@code ChunkCacheMixin}. fastutil's map mixes the hash bits.
 */
@Mixin(MultiplayerChunkCache.class)
class MultiplayerChunkCacheMixin {
    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/world/chunk/MultiplayerChunkCache;chunksByPos:Ljava/util/Map;",
                    opcode = Opcodes.PUTFIELD
            )
    )
    private void stationapi_useFastutilMap(
            MultiplayerChunkCache instance, Map<ChunkPos, Chunk> map, Operation<Void> original
    ) {
        original.call(instance, new Object2ReferenceOpenHashMap<>(map));
    }
}
