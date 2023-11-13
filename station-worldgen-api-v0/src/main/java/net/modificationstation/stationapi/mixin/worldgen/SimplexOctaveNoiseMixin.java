package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.class_458;
import net.minecraft.class_459;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(class_458.class)
class SimplexOctaveNoiseMixin {
    @Shadow private class_459[] field_1746;
    @Shadow private int field_1747;

    // Noise fill optimisation
    // Required for advanced worldgen
    // Speeds up data generation up to 100+ times
    // Yes, it is a fix for manual array filling for the whole length, that solves all issues
    @Inject(
            method = "method_1517",
            at = @At("HEAD"),
            cancellable = true
    )
    private void stationapi_optimizeSample(double[] data, double x, double y, int dx, int dy, double f, double g, double h, double k, CallbackInfoReturnable<double[]> info) {
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

        for (short index = 0; index < this.field_1747; index++) {
            this.field_1746[index].method_1759(data, x, y, dx, dy, f * d3, g * d3, 0.55 / d2);
            d3 *= h;
            d2 *= k;
        }

        info.setReturnValue(data);
    }
}
