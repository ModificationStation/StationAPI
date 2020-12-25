package net.modificationstation.stationapi.mixin.common;

import net.minecraft.level.Level;
import net.minecraft.level.chunk.ServerChunkCache;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationapi.api.common.event.level.gen.ChunkPopulator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ServerChunkCache.class)
public class MixinServerChunkCache {

    @Shadow
    private Level field_1231;

    @Shadow
    private LevelSource field_1227;
    private Random modRandom;

    @Inject(method = "decorate(Lnet/minecraft/level/source/LevelSource;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/source/LevelSource;decorate(Lnet/minecraft/level/source/LevelSource;II)V", shift = At.Shift.AFTER))
    private void onPopulate(LevelSource levelSource, int chunkX, int chunkZ, CallbackInfo ci) {
        int blockX = chunkX * 16;
        int blockZ = chunkZ * 16;
        if (modRandom == null)
            modRandom = new Random();
        modRandom.setSeed(field_1231.getSeed());
        long xRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        long zRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        modRandom.setSeed((long) chunkX * xRandomMultiplier + (long) chunkZ * zRandomMultiplier ^ field_1231.getSeed());
        ChunkPopulator.EVENT.getInvoker().populate(field_1231, field_1227, field_1231.getBiomeSource().getBiome(blockX + 16, blockZ + 16), blockX, blockZ, modRandom);
    }
}
