package net.modificationstation.stationapi.api.worldgen.biomeprovider;

import net.minecraft.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class ClimateBiomeProvider implements BiomeProvider {
	private final List<BiomeInfo> biomes = new ArrayList<>();
	
	/**
	 * Adds a biome with climate parameters to the list
	 * @param biome {@link Biome} to add
	 * @param t1 minimum temperature
	 * @param t2 maximum temperature
	 * @param w1 minimum wetness (rainfall)
	 * @param w2 maximum wetness (rainfall)
	 */
	public void addBiome(Biome biome, float t1, float t2, float w1, float w2) {
		biomes.add(new BiomeInfo(biome, t1, t2, w1, w2));
	}
	
	@Override
	public Biome getBiome(int x, int z, float temperature, float wetness) {
		Biome biome = getBiome(temperature, wetness);
		return biome == null ? biomes.get(0).biome : biome;
	}
	
	protected Biome getBiome(float temperature, float wetness) {
		for (BiomeInfo info : biomes) {
			if (info.t1 > temperature || info.t2 < temperature) continue;
			if (info.w1 > wetness || info.w2 < wetness) continue;
			return info.biome;
		}
		return null;
	}
	
	private record BiomeInfo(
		Biome biome, float t1, float t2, float w1, float w2
	) {}
}
