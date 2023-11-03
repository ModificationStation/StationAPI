package net.modificationstation.stationapi.api.worldgen.structure;

import java.util.Random;
import net.minecraft.class_239;
import net.minecraft.world.World;

public class WeightedStructure extends class_239 {
    private final class_239 structure;
    private final int weight;

    public WeightedStructure(class_239 structure, int weight) {
        this.structure = structure;
        this.weight = weight;
    }

    @Override
    public boolean method_1142(World level, Random random, int x, int y, int z) {
        if (random.nextInt(weight) > 0) return false;
        return structure.method_1142(level, random, x, y, z);
    }
}
