package net.modificationstation.stationapi.impl.worldgen;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.worldgen.biomeprovider.ClimateBiomeProvider;

public class OverworldBiomeProviderImpl extends ClimateBiomeProvider {
	private static final OverworldBiomeProviderImpl INSTANCE = new OverworldBiomeProviderImpl();
	
	private OverworldBiomeProviderImpl() {}
	
	@Override
	public Biome getBiome(int x, int z, float temperature, float wetness) {
		Biome biome = getBiome(temperature, wetness);
		return biome == null ? Biome.getBiome(temperature, wetness) : biome;
	}
	
	@Override
	protected Biome getBiome(float temperature, float wetness) {
		Biome biome = super.getBiome(temperature, wetness);
		return biome == null ? Biome.getBiome(temperature, wetness) : biome;
	}
	
	public static OverworldBiomeProviderImpl getInstance() {
		return INSTANCE;
	}
}
