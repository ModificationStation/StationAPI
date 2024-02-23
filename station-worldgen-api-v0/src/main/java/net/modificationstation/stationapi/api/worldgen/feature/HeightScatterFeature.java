package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class HeightScatterFeature extends ScatterFeature {
    public HeightScatterFeature(Feature feature, int iterations) {
        super(feature, iterations);
    }

    @Override
    protected int getHeight(World world, Random random, int x, int y, int z) {
        return world.getTopY(x, z);
    }
}
