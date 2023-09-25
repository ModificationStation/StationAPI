package net.modificationstation.stationapi.mixin.worldgen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.gen.BiomeSource;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeSource.class)
public class MixinBiomeSource {
	@Unique private long seed;
	@Unique private int posX;
	@Unique private int posZ;
	
	@Inject(method = "<init>(Lnet/minecraft/level/Level;)V", at = @At("TAIL"))
	private void onInit(Level level, CallbackInfo info) {
		// Call this to force biome registry event happen before init of regions
		//noinspection ResultOfMethodCallIgnored
		Biome.getBiome(0, 0);
		BiomeAPI.init(seed);
	}
	
	@Inject(
		method = "getBiomes([Lnet/minecraft/level/biome/Biome;IIII)[Lnet/minecraft/level/biome/Biome;",
		at = @At("HEAD")
	)
	private void capturePosition(Biome[] data, int x, int y, int dx, int dy, CallbackInfoReturnable<Biome[]> info) {
		posX = x;
		posZ = y;
	}
	
	@WrapOperation(method = "getBiomes([Lnet/minecraft/level/biome/Biome;IIII)[Lnet/minecraft/level/biome/Biome;", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/biome/Biome;getBiome(DD)Lnet/minecraft/level/biome/Biome;"
	))
	private Biome getRegionBiome(double temperature, double wetness, Operation<Biome> original, @Local(index = 7) LocalIntRef dx, @Local(index = 8) LocalIntRef dz) {
		int deltaX = dx.get();
		int deltaZ = dz.get();
		BiomeProvider provider = BiomeAPI.getOverworldProvider();
		return provider.getBiome(posX + deltaX, posZ + deltaZ, (float) temperature, (float) wetness);
	}
}