package net.modificationstation.stationapi.mixin.worldgen;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.SandBlock;
import net.minecraft.class_153;
import net.minecraft.class_51;
import net.minecraft.class_538;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import net.modificationstation.stationapi.impl.worldgen.WorldGeneratorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_538.class)
public class MixinOverworldLevelSource {
    @Shadow
    private World level;
    @Shadow
    private double[] noises;

    @Inject(
            method = "decorate",
            at = @At("HEAD")
    )
    private void decorateSurface(class_51 source, int cx, int cz, CallbackInfo info) {
        WorldDecoratorImpl.decorate(this.level, cx, cz);
    }
    
    @Inject(
        method = "decorate",
        at = @At(value = "INVOKE", target = "Ljava/util/Random;setSeed(J)V", ordinal = 0, shift = Shift.BEFORE),
        cancellable = true
    )
    private void cancelStructureGeneration(class_51 source, int cx, int cz, CallbackInfo info, @Local class_153 biome) {
        if (biome.isNoDimensionStrucutres()) {
            SandBlock.field_375 = false;
            info.cancel();
        }
    }

    @ModifyConstant(
            method = "buildSurface",
            constant = @Constant(intValue = 127)
    )
    private int cancelSurfaceMaking(int constant, @Local class_153 biome) {
        return biome.noSurfaceRules() ? level.getTopY() - 1 : -1;
    }

    @Inject(
            method = "shapeChunk",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/level/source/OverworldLevelSource;calculateNoise([DIIIIII)[D",
                    shift = Shift.AFTER
            )
    )
    private void changeHeight(int cx, int cz, byte[] args, class_153[] biomes, double[] par5, CallbackInfo info) {
        WorldGeneratorImpl.updateNoise(level, cx, cz, this.noises);
    }
}
