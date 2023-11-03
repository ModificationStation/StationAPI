package net.modificationstation.stationapi.api.worldgen.structure;

import java.util.Random;
import net.minecraft.class_239;
import net.minecraft.world.World;

public class VolumetricScatterStructure extends ScatterStructure {
    protected final int minHeight;
    protected final int deltaHeight;

    public VolumetricScatterStructure(class_239 structure, int iterations, int maxHeight) {
        this(structure, iterations, 0, maxHeight);
    }

    public VolumetricScatterStructure(class_239 structure, int iterations, int minHeight, int maxHeight) {
        super(structure, iterations);
        this.minHeight = minHeight;
        this.deltaHeight = maxHeight - minHeight + 1;
    }

    @Override
    protected int getHeight(World level, Random random, int x, int y, int z) {
        return minHeight + random.nextInt(deltaHeight);
    }
}
