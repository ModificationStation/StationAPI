package net.modificationstation.stationapi.mixin.worldgen;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.modificationstation.stationapi.api.worldgen.BiomeAPI;
import net.modificationstation.stationapi.impl.world.StationDimension;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collection;

@Mixin(OverworldDimension.class)
class OverworldDimensionMixin extends Dimension implements StationDimension {
    @Override
    public Collection<Biome> getBiomes() {
        return BiomeAPI.getOverworldProvider().getBiomes();
    }
}
