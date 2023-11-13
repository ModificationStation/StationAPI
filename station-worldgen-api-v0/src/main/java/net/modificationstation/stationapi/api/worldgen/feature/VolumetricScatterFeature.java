package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.class_239;
import net.minecraft.world.World;

import java.util.Random;

public class VolumetricScatterFeature extends ScatterFeature {
    protected final int minHeight;
    protected final int deltaHeight;

    public VolumetricScatterFeature(class_239 feature, int iterations, int maxHeight) {
        this(feature, iterations, 0, maxHeight);
    }

    public VolumetricScatterFeature(class_239 feature, int iterations, int minHeight, int maxHeight) {
        super(feature, iterations);
        this.minHeight = minHeight;
        this.deltaHeight = maxHeight - minHeight + 1;
    }

    @Override
    protected int getHeight(World world, Random random, int x, int y, int z) {
        return minHeight + random.nextInt(deltaHeight);
    }
}
