package net.modificationstation.stationapi.mixin.level.client;

import net.minecraft.class_390;
import net.minecraft.class_51;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.gen.LevelGenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(class_390.class)
public class MixinChunkCache {

    @Shadow
    private World level;

    @Shadow
    private class_51 levelSource;
    private Random modRandom;

    @Inject(method = "decorate(Lnet/minecraft/level/source/LevelSource;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/source/LevelSource;decorate(Lnet/minecraft/level/source/LevelSource;II)V", shift = At.Shift.AFTER))
    private void onPopulate(class_51 levelSource, int chunkX, int chunkZ, CallbackInfo ci) {
        int blockX = chunkX * 16;
        int blockZ = chunkZ * 16;
        if (modRandom == null)
            modRandom = new Random();
        modRandom.setSeed(level.getSeed());
        long xRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        long zRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        modRandom.setSeed((long) chunkX * xRandomMultiplier + (long) chunkZ * zRandomMultiplier ^ level.getSeed());
        StationAPI.EVENT_BUS.post(
                LevelGenEvent.ChunkDecoration.builder()
                        .level(level)
                        .levelSource(this.levelSource)
                        .biome(level.method_1781().method_1787(blockX + 16, blockZ + 16))
                        .x(blockX).z(blockZ)
                        .random(modRandom)
                        .build()
        );
    }
}
