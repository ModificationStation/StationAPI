package net.modificationstation.stationapi.mixin.biome;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_153;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.world.biome.BiomeByClimateEvent;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_153.class)
class BiomeMixin {
    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_153;method_785()V",
                    shift = At.Shift.BEFORE
            )
    )
    private static void stationapi_afterVanillaBiomes(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(BiomeRegisterEvent.builder().build());
    }

    @ModifyReturnValue(
            method = "method_787",
            at = @At("RETURN")
    )
    private static class_153 stationapi_getBiome(class_153 currentBiome, float temperature, float downfall) {
        return StationAPI.EVENT_BUS.post(
                BiomeByClimateEvent.builder()
                        .temperature(temperature)
                        .downfall(downfall)
                        .currentBiome(currentBiome)
                        .build()
        ).currentBiome;
    }
}
