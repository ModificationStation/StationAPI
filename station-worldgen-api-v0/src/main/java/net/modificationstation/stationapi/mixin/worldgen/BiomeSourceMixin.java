package net.modificationstation.stationapi.mixin.worldgen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.api.worldgen.biome.BiomeProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeSource.class)
class BiomeSourceMixin {
    @Inject(
            method = "getBiomesInArea",
            at = @At("HEAD")
    )
    private void stationapi_capturePosition(
            Biome[] data, int x, int z, int dx, int dz, CallbackInfoReturnable<Biome[]> info,
            @Share("posX") LocalIntRef posX, @Share("posZ") LocalIntRef posZ
    ) {
        posX.set(x);
        posZ.set(z);
    }

    @WrapOperation(
            method = "getBiomesInArea",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/biome/Biome;getBiome(DD)Lnet/minecraft/world/biome/Biome;"
            )
    )
    private Biome stationapi_getRegionBiome(
            double temperature, double wetness, Operation<Biome> original,
            @Local(index = 7) int dx, @Local(index = 8) int dz,
            @Share("posX") LocalIntRef posX, @Share("posZ") LocalIntRef posZ
    ) {
        BiomeProvider provider = BiomeAPI.getOverworldProvider();
        return provider.getBiome(posX.get() + dx, posZ.get() + dz, (float) temperature, (float) wetness);
    }
}
