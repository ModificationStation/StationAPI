package net.modificationstation.stationapi.mixin.level.client;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.ChunkCache;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.gen.LevelGenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ChunkCache.class)
public class MixinChunkCache {

    @Shadow
    private Level level;

    @Shadow
    private LevelSource levelSource;
    private Random modRandom;

    @Inject(method = "decorate(Lnet/minecraft/level/source/LevelSource;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/source/LevelSource;decorate(Lnet/minecraft/level/source/LevelSource;II)V", shift = At.Shift.AFTER))
    private void onPopulate(LevelSource levelSource, int chunkX, int chunkZ, CallbackInfo ci) {
        int blockX = chunkX * 16;
        int blockZ = chunkZ * 16;
        if (modRandom == null)
            modRandom = new Random();
        modRandom.setSeed(level.getSeed());
        long xRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        long zRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        modRandom.setSeed((long) chunkX * xRandomMultiplier + (long) chunkZ * zRandomMultiplier ^ level.getSeed());
        StationAPI.EVENT_BUS.post(new LevelGenEvent.ChunkDecoration(level, this.levelSource, level.getBiomeSource().getBiome(blockX + 16, blockZ + 16), blockX, blockZ, modRandom));
    }
}
