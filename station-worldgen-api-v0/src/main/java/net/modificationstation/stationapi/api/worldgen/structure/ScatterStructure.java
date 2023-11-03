package net.modificationstation.stationapi.api.worldgen.structure;

import java.util.Random;
import net.minecraft.class_239;
import net.minecraft.world.World;

public abstract class ScatterStructure extends class_239 {
    protected final class_239 structure;
    protected final int iterations;

    public ScatterStructure(class_239 structure, int iterations) {
        this.structure = structure;
        this.iterations = iterations;
    }

    @Override
    public boolean method_1142(World level, Random random, int x, int y, int z) {
        boolean result = false;
        for (int i = 0; i < iterations; i++) {
            int px = x + random.nextInt(16);
            int pz = z + random.nextInt(16);
            int py = getHeight(level, random, px, y, pz);
            result = structure.method_1142(level, random, px, py, pz) | result;
        }
        return result;
    }

    protected abstract int getHeight(World level, Random random, int x, int y, int z);
}
