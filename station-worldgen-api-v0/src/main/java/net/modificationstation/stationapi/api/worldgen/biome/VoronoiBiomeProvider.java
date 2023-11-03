package net.modificationstation.stationapi.api.worldgen.biome;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.class_153;
import net.minecraft.class_458;
import net.modificationstation.stationapi.impl.worldgen.IDVoronoiNoise;

import java.util.*;

/**
 * Provides uniform biomes distribution based on voronoi cells with distortion
 */
public class VoronoiBiomeProvider implements BiomeProvider {
    private final List<class_153> biomes = new ArrayList<>();
    private final double[] buffer = new double[1];

    private IDVoronoiNoise idNoise;
    private class_458 distortX;
    private class_458 distortZ;
    private final double distortion;
    private final double s1;
    private final double s2;

    public VoronoiBiomeProvider() {
        this(100, 0.1);
    }

    public VoronoiBiomeProvider(double scale) {
        this(scale, 0.1);
    }

    public VoronoiBiomeProvider(double scale, double distortion) {
        this.distortion = distortion;
        s1 = 1.0 / scale;
        s2 = s1 * 10.0;
    }

    public void addBiome(class_153 biome) {
        biomes.add(biome);
    }

    @Override
    public class_153 getBiome(int x, int z, float temperature, float wetness) {
        double px = x * s1 + distortX.method_1516(buffer, x, z, 1, 1, s2, s2, 0.25)[0] * distortion;
        double pz = z * s1 + distortZ.method_1516(buffer, x, z, 1, 1, s2, s2, 0.25)[0] * distortion;
        int id = idNoise.getID(px, pz, biomes.size());
        return biomes.get(id);
    }
    
    @Override
    public Collection<class_153> getBiomes() {
        Set<class_153> biomes = new ObjectOpenHashSet<>();
        biomes.addAll(this.biomes);
        return biomes;
    }

    @Override
    public void setSeed(long seed) {
        Random random = new Random(seed);
        idNoise = new IDVoronoiNoise(random.nextInt());
        distortX = new class_458(new Random(random.nextLong()), 2);
        distortZ = new class_458(new Random(random.nextLong()), 2);
    }
}
