package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.block.SandBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.gen.chunk.NetherChunkGenerator;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherChunkGenerator.class)
class NetherWorldSourceMixin {
    @Shadow private World field_1350;

    @Inject(
            method = "method_1803",
            at = @At("HEAD")
    )
    private void stationapi_makeSurface(ChunkSource source, int cx, int cz, CallbackInfo info) {
        WorldDecoratorImpl.decorate(this.field_1350, cx, cz);
    }
    
    @Inject(
        method = "method_1803",
        at = @At(value = "FIELD", target = "Lnet/minecraft/block/SandBlock;field_375:Z", ordinal = 0, shift = Shift.BEFORE),
        cancellable = true
    )
    private void stationapi_cancelFeatureGeneration(ChunkSource source, int cx, int cz, CallbackInfo info) {
        Biome biome = this.field_1350.method_1781().getBiome(cx + 16, cz + 16);
        if (biome.isNoDimensionFeatures()) {
            SandBlock.fallInstantly = false;
            info.cancel();
        }
    }
}
