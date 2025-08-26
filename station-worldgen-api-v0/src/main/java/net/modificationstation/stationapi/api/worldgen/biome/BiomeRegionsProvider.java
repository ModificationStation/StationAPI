package net.modificationstation.stationapi.api.worldgen.biome;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.impl.worldgen.IDVoronoiNoise;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BiomeRegionsProvider implements BiomeProvider {
    private final double[] buffer = new double[1];
    private final List<BiomeProvider> providers;

    private IDVoronoiNoise idNoise;
    private OctaveSimplexNoiseSampler distortX;
    private OctaveSimplexNoiseSampler distortZ;

    public BiomeRegionsProvider(List<BiomeProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Biome getBiome(int x, int z, float temperature, float downfall) {
        double px = x * 0.01 + distortX.sample(buffer, x, z, 1, 1, 0.1, 0.1, 0.25)[0] * 0.1;
        double pz = z * 0.01 + distortZ.sample(buffer, x, z, 1, 1, 0.1, 0.1, 0.25)[0] * 0.1;
        int id = idNoise.getID(px, pz, providers.size());
        return providers.get(id).getBiome(x, z, temperature, downfall);
    }
    
    @Override
    public Collection<Biome> getBiomes() {
        Set<Biome> biomes = new ObjectOpenHashSet<>();
        providers.forEach(provider -> biomes.addAll(provider.getBiomes()));
        return biomes;
    }
    
    @Override
    public void setSeed(long seed) {
        Random random = new Random(seed);
        idNoise = new IDVoronoiNoise(random.nextInt());
        distortX = new OctaveSimplexNoiseSampler(new Random(random.nextLong()), 2);
        distortZ = new OctaveSimplexNoiseSampler(new Random(random.nextLong()), 2);
        providers.forEach(provider -> provider.setSeed(seed));
    }
}
