package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.BiomeProvider;

public class OverworldBiomeProvider implements BiomeProvider {
	private static final OverworldBiomeProvider INSTANCE = new OverworldBiomeProvider();
	
	private OverworldBiomeProvider() {}
	
	@Override
	public Biome getBiome(int x, int z, float temperature, float wetness) {
		return Biome.getBiome(temperature, wetness);
	}
	
	public static OverworldBiomeProvider getInstance() {
		return INSTANCE;
	}
}
