package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.class_239;
import net.minecraft.world.World;

import java.util.Random;

public abstract class ScatterFeature extends class_239 {
    protected final class_239 feature;
    protected final int iterations;

    public ScatterFeature(class_239 feature, int iterations) {
        this.feature = feature;
        this.iterations = iterations;
    }

    @Override
    public boolean method_1142(World world, Random random, int x, int y, int z) {
        boolean result = false;
        for (int i = 0; i < iterations; i++) {
            int px = x + random.nextInt(16);
            int pz = z + random.nextInt(16);
            int py = getHeight(world, random, px, y, pz);
            result = feature.method_1142(world, random, px, py, pz) | result;
        }
        return result;
    }

    protected abstract int getHeight(World world, Random random, int x, int y, int z);
}
