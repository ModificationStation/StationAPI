package net.modificationstation.stationapi.api.worldgen.feature;

import net.minecraft.class_239;
import net.minecraft.world.World;

import java.util.Random;

public class WeightedFeature extends class_239 {
    private final class_239 feature;
    private final int weight;

    public WeightedFeature(class_239 feature, int weight) {
        this.feature = feature;
        this.weight = weight;
    }

    @Override
    public boolean method_1142(World world, Random random, int x, int y, int z) {
        if (random.nextInt(weight) > 0) return false;
        return feature.method_1142(world, random, x, y, z);
    }
}
