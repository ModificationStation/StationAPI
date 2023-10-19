package net.modificationstation.stationapi.api.worldgen.structure;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.util.Random;

public abstract class ScatterStructure extends Structure {
    protected final Structure structure;
    protected final int iterations;

    public ScatterStructure(Structure structure, int iterations) {
        this.structure = structure;
        this.iterations = iterations;
    }

    @Override
    public boolean generate(Level level, Random random, int x, int y, int z) {
        boolean result = false;
        for (int i = 0; i < iterations; i++) {
            int px = x + random.nextInt(16);
            int pz = z + random.nextInt(16);
            int py = getHeight(level, random, px, y, pz);
            result = structure.generate(level, random, px, py, pz) | result;
        }
        return result;
    }

    protected abstract int getHeight(Level level, Random random, int x, int y, int z);
}
