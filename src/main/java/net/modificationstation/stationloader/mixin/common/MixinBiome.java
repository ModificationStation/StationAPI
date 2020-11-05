package net.modificationstation.stationloader.mixin.common;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationloader.api.common.event.level.biome.BiomeByClimateProvider;
import net.modificationstation.stationloader.api.common.event.level.biome.BiomeRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(Biome.class)
public class MixinBiome {

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/level/biome/Biome;createBiomeArray()V", shift = At.Shift.BEFORE))
    private static void afterVanillaBiomes(CallbackInfo ci) {
        BiomeRegister.EVENT.getInvoker().registerBiomes();
    }

    @Inject(method = "getClimateBiome(FF)Lnet/minecraft/level/biome/Biome;", at = @At("RETURN"))
    private static void getBiome(float temperature, float rainfall, CallbackInfoReturnable<Biome> cir) {
        AtomicReference<Biome> biome = new AtomicReference<>(cir.getReturnValue());
        BiomeByClimateProvider.EVENT.getInvoker().getBiome(biome, temperature, rainfall);
        cir.setReturnValue(biome.get());
    }
}
