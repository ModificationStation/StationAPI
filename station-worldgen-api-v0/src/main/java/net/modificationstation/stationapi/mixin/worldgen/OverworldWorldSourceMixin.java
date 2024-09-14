package net.modificationstation.stationapi.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.SandBlock;
import net.minecraft.class_51;
import net.minecraft.class_538;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import net.modificationstation.stationapi.impl.worldgen.WorldGeneratorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = class_538.class, priority = 2000)
class OverworldWorldSourceMixin {
    @Shadow
    private World field_2260;
    @Shadow
    private double[] field_2261;

    @Inject(
            method = "method_1803",
            at = @At("HEAD")
    )
    private void stationapi_decorateSurface(class_51 source, int cx, int cz, CallbackInfo info) {
        WorldDecoratorImpl.decorate(this.field_2260, cx, cz);
    }
    
    @Inject(
        method = "method_1803",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;setSeed(J)V", ordinal = 0, shift = Shift.BEFORE),
        cancellable = true
    )
    private void stationapi_cancelFeatureGeneration(class_51 source, int cx, int cz, CallbackInfo info, @Local Biome biome) {
        if (biome.isNoDimensionFeatures()) {
            SandBlock.field_375 = false;
            info.cancel();
        }
    }

    @ModifyExpressionValue(
            method = "method_1797",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=127"
            )
    )
    private int stationapi_cancelSurfaceMaking(int constant, @Local Biome biome) {
        return biome.noSurfaceRules() ? constant : Integer.MIN_VALUE;
    }

    @Inject(
            method = "method_1798",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/class_538;method_1799([DIIIIII)[D",
                    shift = Shift.AFTER
            )
    )
    private void stationapi_changeHeight(int cx, int cz, byte[] args, Biome[] biomes, double[] par5, CallbackInfo info) {
        WorldGeneratorImpl.updateNoise(field_2260, cx, cz, this.field_2261);
    }
}
