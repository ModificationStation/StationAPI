package net.modificationstation.stationapi.mixin.world.client;

import net.minecraft.class_390;
import net.minecraft.class_51;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.world.gen.WorldGenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(class_390.class)
class ChunkCacheMixin {
    @Shadow
    private World field_1515;

    @Shadow
    private class_51 field_1512;
    @Unique
    private Random modRandom;

    @Inject(
            method = "method_1803",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_51;method_1803(Lnet/minecraft/class_51;II)V",
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_onPopulate(class_51 worldSource, int chunkX, int chunkZ, CallbackInfo ci) {
        int blockX = chunkX * 16;
        int blockZ = chunkZ * 16;
        if (modRandom == null)
            modRandom = new Random();
        modRandom.setSeed(field_1515.getSeed());
        long xRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        long zRandomMultiplier = (modRandom.nextLong() / 2L) * 2L + 1L;
        modRandom.setSeed((long) chunkX * xRandomMultiplier + (long) chunkZ * zRandomMultiplier ^ field_1515.getSeed());
        StationAPI.EVENT_BUS.post(
                WorldGenEvent.ChunkDecoration.builder()
                        .world(field_1515)
                        .worldSource(this.field_1512)
                        .biome(field_1515.method_1781().method_1787(blockX + 16, blockZ + 16))
                        .x(blockX).z(blockZ)
                        .random(modRandom)
                        .build()
        );
    }
}
