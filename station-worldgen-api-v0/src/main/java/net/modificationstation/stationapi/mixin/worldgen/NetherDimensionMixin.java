package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.NetherDimension;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.impl.worldgen.NetherBiomeSourceImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(NetherDimension.class)
class NetherDimensionMixin extends Dimension {
    @Inject(
            method = "initBiomeSource",
            at = @At("TAIL")
    )
    private void stationapi_setNetherBiomeSource(CallbackInfo info) {
        this.biomeSource = NetherBiomeSourceImpl.getInstance();
    }
    
    @Override
    public Collection<Biome> getBiomes() {
        return BiomeAPI.getNetherProvider().getBiomes();
    }
}
