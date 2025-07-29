package net.modificationstation.stationapi.mixin.world.client;

import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.LegacyChunkCache;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.world.gen.WorldGenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LegacyChunkCache.class)
class ChunkCacheMixin {
    @Shadow
    private World world;

    @Shadow
    private ChunkSource generator;
    @Unique
    private Random modRandom;

    @Inject(
            method = "decorate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/ChunkSource;decorate(Lnet/minecraft/world/chunk/ChunkSource;II)V",
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_onPopulate(ChunkSource worldSource, int chunkX, int chunkZ, CallbackInfo ci) {
        int blockX = chunkX * 16;
        int blockZ = chunkZ * 16;
        if (modRandom == null)
            modRandom = new Random();
        modRandom.setSeed(world.getSeed());
        long xRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        long zRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        modRandom.setSeed((long) chunkX * xRandomMultiplier + (long) chunkZ * zRandomMultiplier ^ world.getSeed());
        StationAPI.EVENT_BUS.post(
                WorldGenEvent.ChunkDecoration.builder()
                        .world(world)
                        .worldSource(this.generator)
                        .biome(world.method_1781().getBiome(blockX + 16, blockZ + 16))
                        .x(blockX).z(blockZ)
                        .random(modRandom)
                        .build()
        );
    }
}
