package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.util.noise.SimplexNoise;
import net.minecraft.util.noise.SimplexOctaveNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(SimplexOctaveNoise.class)
public class MixinSimplexOctaveNoise {
    @Shadow private SimplexNoise[] generators;
    @Shadow private int octaves;

    // Noise fill optimisation
    // Required for advanced worldgen
    // Speeds up data generation up to 100+ times
    // Yes, it is a fix for manual array filling for the whole length, that solves all issues
    @Inject(
            method = "sample([DDDIIDDDD)[D",
            at = @At("HEAD"),
            cancellable = true
    )
    private void optimizeSample(double[] data, double x, double y, int dx, int dy, double f, double g, double h, double k, CallbackInfoReturnable<double[]> info) {
        f /= 1.5;
        g /= 1.5;

        int length = dx * dy;
        if (data == null || data.length < length) {
            data = new double[length];
        } else {
            Arrays.fill(data, 0, length, 0.0);
        }

        double d2 = 1.0;
        double d3 = 1.0;

        for (short index = 0; index < this.octaves; index++) {
            this.generators[index].sample(data, x, y, dx, dy, f * d3, g * d3, 0.55 / d2);
            d3 *= h;
            d2 *= k;
        }

        info.setReturnValue(data);
    }
}
