package net.modificationstation.stationapi.api.worldgen.structure;

import java.util.Random;
import net.minecraft.class_239;
import net.minecraft.world.World;

/**
 * Scatter feature in "levels" (each 16 blocks), should be good for caves and Nether
 */
public class LeveledScatterStructure extends ScatterStructure {
    public LeveledScatterStructure(class_239 structure, int iterations) {
        super(structure, iterations);
    }

    @Override
    public boolean method_1142(World level, Random random, int x, int y, int z) {
        boolean result = false;
        int minY = level.getBottomY() >> 4;
        int maxY = level.getTopY() >> 4;
        for (int index = minY; index < maxY; index++) {
            for (int i = 0; i < iterations; i++) {
                int px = x + random.nextInt(16);
                int pz = z + random.nextInt(16);
                int py = getHeight(level, random, px, index << 4, pz);
                if (py == Integer.MIN_VALUE) continue;
                result = structure.method_1142(level, random, px, py, pz) | result;
            }
        }
        return result;
    }

    @Override
    protected int getHeight(World level, Random random, int x, int y, int z) {
        boolean opaque = level.getBlockState(x, y, z).getBlock().isOpaque();
        for (int i = 1; i < 16; i++) {
            boolean opaque2 = level.getBlockState(x, --y, z).getBlock().isOpaque();
            if (opaque2 && !opaque) return y + 1;
        }
        return Integer.MIN_VALUE;
    }
}
