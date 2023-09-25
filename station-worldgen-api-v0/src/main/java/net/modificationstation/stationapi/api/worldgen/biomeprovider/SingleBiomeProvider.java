package net.modificationstation.stationapi.api.worldgen.biomeprovider;

import net.minecraft.level.biome.Biome;

public class SingleBiomeProvider implements BiomeProvider {
	private final Biome biome;
	
	public SingleBiomeProvider(Biome biome) {
		this.biome = biome;
	}
	
	@Override
	public Biome getBiome(int x, int z, float temperature, float wetness) {
		return biome;
	}
}
