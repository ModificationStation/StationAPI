package net.modificationstation.stationapi.api.worldgen.biome;

import net.minecraft.level.biome.Biome;
import net.minecraft.util.noise.SimplexOctaveNoise;
import net.modificationstation.stationapi.impl.worldgen.IDVoronoiNoise;

import java.util.List;
import java.util.Random;

public class BiomeRegionsProvider implements BiomeProvider {
    private final double[] buffer = new double[1];
    private final List<BiomeProvider> providers;

    private IDVoronoiNoise idNoise;
    private SimplexOctaveNoise distortX;
    private SimplexOctaveNoise distortZ;

    public BiomeRegionsProvider(List<BiomeProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Biome getBiome(int x, int z, float temperature, float wetness) {
        double px = x * 0.01 + distortX.sample(buffer, x, z, 1, 1, 0.1, 0.1, 0.25)[0] * 0.1;
        double pz = z * 0.01 + distortZ.sample(buffer, x, z, 1, 1, 0.1, 0.1, 0.25)[0] * 0.1;
        int id = idNoise.getID(px, pz, providers.size());
        return providers.get(id).getBiome(x, z, temperature, wetness);
    }

    @Override
    public void setSeed(long seed) {
        Random random = new Random(seed);
        idNoise = new IDVoronoiNoise(random.nextInt());
        distortX = new SimplexOctaveNoise(new Random(random.nextLong()), 2);
        distortZ = new SimplexOctaveNoise(new Random(random.nextLong()), 2);
        providers.forEach(provider -> provider.setSeed(seed));
    }
}
