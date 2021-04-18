package io.github.minecraftcursedlegacy.mixin.terrain;

import java.util.concurrent.atomic.AtomicReference;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.minecraftcursedlegacy.api.event.ActionResult;
import io.github.minecraftcursedlegacy.api.terrain.BiomeEvents.BiomePlacementCallback;
import net.minecraft.level.biome.Biome;

@Mixin(Biome.class)
public class MixinBiome {
	@Inject(at = @At("HEAD"), method = "getClimateBiome", cancellable = true)
	private static void addModdedBiomeGen(float temperature, float humidity, CallbackInfoReturnable<Biome> info) {
		AtomicReference<Biome> biomeResultWrapper = new AtomicReference<>();
		ActionResult result = BiomePlacementCallback.EVENT.invoker().onClimaticBiomePlace(temperature, humidity, biomeResultWrapper::set);

		if (result == ActionResult.SUCCESS) {
			Biome biomeResult = biomeResultWrapper.get();

			if (biomeResult != null) {
				info.setReturnValue(biomeResult);
			}
		}
	}
}
