package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public abstract class ScatterFeature extends Feature {
    protected final Feature feature;
    protected final int iterations;

    public ScatterFeature(Feature feature, int iterations) {
        this.feature = feature;
        this.iterations = iterations;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        boolean result = false;
        for (int i = 0; i < iterations; i++) {
            int px = x + random.nextInt(16);
            int pz = z + random.nextInt(16);
            int py = getHeight(world, random, px, y, pz);
            result = feature.generate(world, random, px, py, pz) | result;
        }
        return result;
    }

    protected abstract int getHeight(World world, Random random, int x, int y, int z);
}
