package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.class_239;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Scatter feature in "levels" (each 16 blocks), should be good for caves and Nether
 */
public class LeveledScatterFeature extends ScatterFeature {
    public LeveledScatterFeature(class_239 feature, int iterations) {
        super(feature, iterations);
    }

    @Override
    public boolean method_1142(World world, Random random, int x, int y, int z) {
        boolean result = false;
        int minY = world.getBottomY() >> 4;
        int maxY = world.getTopY() >> 4;
        for (int index = minY; index < maxY; index++) {
            for (int i = 0; i < iterations; i++) {
                int px = x + random.nextInt(16);
                int pz = z + random.nextInt(16);
                int py = getHeight(world, random, px, index << 4, pz);
                if (py == Integer.MIN_VALUE) continue;
                result = feature.method_1142(world, random, px, py, pz) | result;
            }
        }
        return result;
    }

    @Override
    protected int getHeight(World world, Random random, int x, int y, int z) {
        boolean opaque = world.getBlockState(x, y, z).getBlock().isOpaque();
        for (int i = 1; i < 16; i++) {
            boolean opaque2 = world.getBlockState(x, --y, z).getBlock().isOpaque();
            if (opaque2 && !opaque) return y + 1;
        }
        return Integer.MIN_VALUE;
    }
}
