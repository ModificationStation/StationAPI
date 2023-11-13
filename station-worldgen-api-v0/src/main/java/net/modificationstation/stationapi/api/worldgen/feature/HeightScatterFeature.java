package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.class_239;
import net.minecraft.world.World;

import java.util.Random;

public class HeightScatterFeature extends ScatterFeature {
    public HeightScatterFeature(class_239 feature, int iterations) {
        super(feature, iterations);
    }

    @Override
    protected int getHeight(World world, Random random, int x, int y, int z) {
        return world.method_222(x, z);
    }
}
