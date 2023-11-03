package net.modificationstation.stationapi.mixin.biome;

import net.minecraft.class_153;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.biome.BiomeByClimateEvent;
import net.modificationstation.stationapi.api.event.level.biome.BiomeRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_153.class)
public class MixinBiome {

    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/biome/Biome;createBiomeArray()V", shift = At.Shift.BEFORE))
    private static void afterVanillaBiomes(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(BiomeRegisterEvent.builder().build());
    }

    @Inject(method = "getClimateBiome(FF)Lnet/minecraft/level/biome/Biome;", at = @At("RETURN"), cancellable = true)
    private static void getBiome(float temperature, float rainfall, CallbackInfoReturnable<class_153> cir) {
        cir.setReturnValue(StationAPI.EVENT_BUS.post(
                BiomeByClimateEvent.builder()
                        .temperature(temperature)
                        .rainfall(rainfall)
                        .currentBiome(cir.getReturnValue())
                        .build()
        ).currentBiome);
    }
}
