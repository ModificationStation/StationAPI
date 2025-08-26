package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.world.biome.source.BiomeSource;

@FunctionalInterface
public interface BiomeColorProvider {
    int getColor(BiomeSource source, int x, int z);
}

