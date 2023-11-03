package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.block.SandBlock;
import net.minecraft.class_153;
import net.minecraft.class_359;
import net.minecraft.class_51;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_359.class)
public class MixinNetherLevelSource {
    @Shadow private World level;

    @Inject(
            method = "decorate",
            at = @At("HEAD")
    )
    private void makeSurface(class_51 source, int cx, int cz, CallbackInfo info) {
        WorldDecoratorImpl.decorate(this.level, cx, cz);
    }
    
    @Inject(
        method = "decorate",
        at = @At(value = "FIELD", target = "Lnet/minecraft/block/Sand;fallInstantly:Z", ordinal = 0, shift = Shift.BEFORE),
        cancellable = true
    )
    private void cancelStructureGeneration(class_51 source, int cx, int cz, CallbackInfo info) {
        class_153 biome = this.level.method_1781().method_1787(cx + 16, cz + 16);
        if (biome.isNoDimensionStrucutres()) {
            SandBlock.field_375 = false;
            info.cancel();
        }
    }
}
