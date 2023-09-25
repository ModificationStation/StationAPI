package net.modificationstation.stationapi.api.worldgen.biomeprovider;

import net.minecraft.level.biome.Biome;
import net.minecraft.util.noise.SimplexOctaveNoise;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.impl.worldgen.IDVoronoiNoise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MultiBiomeProvider implements BiomeProvider {
	private final List<BiomeProvider> providers;
	private final IDVoronoiNoise idNoise;
	private final SimplexOctaveNoise distortX;
	private final SimplexOctaveNoise distortZ;
	private final double[] buffer = new double[1];
	private final long seed;
	
	public MultiBiomeProvider(long seed, Map<Identifier, BiomeProvider> providers) {
		final List<BiomeProvider> preProviders = new ArrayList<>();
		providers.keySet().stream().sorted().forEach(key -> {
			preProviders.add(providers.get(key));
		});
		this.providers = Collections.unmodifiableList(preProviders);
		
		Random random = new Random(seed);
		idNoise = new IDVoronoiNoise(random.nextInt());
		distortX = new SimplexOctaveNoise(new Random(random.nextLong()), 2);
		distortZ = new SimplexOctaveNoise(new Random(random.nextLong()), 2);
		
		this.seed = seed;
	}
	
	@Override
	public Biome getBiome(int x, int z, float temperature, float wetness) {
		double px = x * 0.03 + distortX.sample(buffer, x, z, 1, 1, 0.1, 0.1, 0.25)[0] * 0.3;
		double pz = z * 0.03 + distortZ.sample(buffer, x, z, 1, 1, 0.1, 0.1, 0.25)[0] * 0.3;
		int id = idNoise.getID(px, pz, providers.size());
		return providers.get(id).getBiome(x, z, temperature, wetness);
	}
	
	public long getSeed() {
		return seed;
	}
}
