package net.modificationstation.stationapi.mixin.common;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.level.biome.BiomeByClimateCallback;
import net.modificationstation.stationapi.api.common.event.level.biome.BiomeRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class MixinBiome {

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/biome/Biome;createBiomeArray()V", shift = At.Shift.BEFORE))
    private static void afterVanillaBiomes(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new BiomeRegister());
    }

    @Inject(method = "getClimateBiome(FF)Lnet/minecraft/level/biome/Biome;", at = @At("RETURN"), cancellable = true)
    private static void getBiome(float temperature, float rainfall, CallbackInfoReturnable<Biome> cir) {
        cir.setReturnValue(StationAPI.EVENT_BUS.post(new BiomeByClimateCallback(cir.getReturnValue(), temperature, rainfall)).currentBiome);
    }
}
