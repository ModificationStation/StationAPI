package net.modificationstation.stationapi.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.SandBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import net.modificationstation.stationapi.impl.worldgen.WorldGeneratorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = OverworldChunkGenerator.class, priority = 2000)
class OverworldWorldSourceMixin {
    @Shadow
    private World world;
    @Shadow
    private double[] heightMap;

    @Inject(
            method = "decorate",
            at = @At("HEAD")
    )
    private void stationapi_decorateSurface(ChunkSource source, int cx, int cz, CallbackInfo info) {
        WorldDecoratorImpl.decorate(this.world, cx, cz);
    }
    
    @Inject(
        method = "decorate",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;setSeed(J)V", ordinal = 0, shift = Shift.BEFORE),
        cancellable = true
    )
    private void stationapi_cancelFeatureGeneration(ChunkSource source, int cx, int cz, CallbackInfo info, @Local Biome biome) {
        if (biome.isNoDimensionFeatures()) {
            SandBlock.fallInstantly = false;
            info.cancel();
        }
    }

    @ModifyExpressionValue(
            method = "buildSurfaces",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=127"
            )
    )
    private int stationapi_cancelSurfaceMaking(int constant, @Local Biome biome) {
        return biome.noSurfaceRules() ? constant : Integer.MIN_VALUE;
    }

    @Inject(
            method = "buildTerrain",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/class_538;generateHeightMap([DIIIIII)[D",
                    shift = Shift.AFTER
            )
    )
    private void stationapi_changeHeight(int cx, int cz, byte[] args, Biome[] biomes, double[] par5, CallbackInfo info) {
        WorldGeneratorImpl.updateNoise(world, cx, cz, this.heightMap);
    }
}
