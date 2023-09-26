package net.modificationstation.stationapi.api.worldgen.biomeprovider;

import net.minecraft.level.gen.BiomeSource;

@FunctionalInterface
public interface BiomeColorProvider {
	int getColor(BiomeSource source, int x, int z);
}

