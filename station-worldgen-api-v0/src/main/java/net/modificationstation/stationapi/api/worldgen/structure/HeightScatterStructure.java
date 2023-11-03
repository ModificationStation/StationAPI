package net.modificationstation.stationapi.api.worldgen.structure;

import java.util.Random;
import net.minecraft.class_239;
import net.minecraft.world.World;

public class HeightScatterStructure extends ScatterStructure {
    public HeightScatterStructure(class_239 structure, int iterations) {
        super(structure, iterations);
    }

    @Override
    protected int getHeight(World level, Random random, int x, int y, int z) {
        return level.method_222(x, z);
    }
}
