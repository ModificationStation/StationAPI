package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class WeightedFeature extends Feature {
    private final Feature feature;
    private final int weight;

    public WeightedFeature(Feature feature, int weight) {
        this.feature = feature;
        this.weight = weight;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        if (random.nextInt(weight) > 0) return false;
        return feature.generate(world, random, x, y, z);
    }
}
