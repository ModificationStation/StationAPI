package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.block.Sand;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.source.NetherLevelSource;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherLevelSource.class)
public class MixinNetherLevelSource {
    @Shadow private Level level;

    @Inject(
            method = "decorate",
            at = @At("HEAD")
    )
    private void makeSurface(LevelSource source, int cx, int cz, CallbackInfo info) {
        WorldDecoratorImpl.decorate(this.level, cx, cz);
    }
    
    @Inject(
        method = "decorate",
        at = @At(value = "FIELD", target = "Lnet/minecraft/block/Sand;fallInstantly:Z", ordinal = 0, shift = Shift.BEFORE),
        cancellable = true
    )
    private void cancelStructureGeneration(LevelSource source, int cx, int cz, CallbackInfo info) {
        Biome biome = this.level.getBiomeSource().getBiome(cx + 16, cz + 16);
        if (biome.isNoDimensionStrucutres()) {
            Sand.fallInstantly = false;
            info.cancel();
        }
    }
}
