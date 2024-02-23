package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class BottomWeightedScatter extends VolumetricScatterFeature {
    public BottomWeightedScatter(Feature feature, int iterations, int minHeight, int maxHeight) {
        super(feature, iterations, minHeight, maxHeight);
    }

    @Override
    protected int getHeight(World world, Random random, int x, int y, int z) {
        return random.nextInt(minHeight + random.nextInt(deltaHeight));
    }
}
