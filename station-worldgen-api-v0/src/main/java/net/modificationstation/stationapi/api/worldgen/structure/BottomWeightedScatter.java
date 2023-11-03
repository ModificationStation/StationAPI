package net.modificationstation.stationapi.api.worldgen.structure;

import java.util.Random;
import net.minecraft.class_239;
import net.minecraft.world.World;

public class BottomWeightedScatter extends VolumetricScatterStructure {
    public BottomWeightedScatter(class_239 structure, int iterations, int minHeight, int maxHeight) {
        super(structure, iterations, minHeight, maxHeight);
    }

    @Override
    protected int getHeight(World level, Random random, int x, int y, int z) {
        return random.nextInt(minHeight + random.nextInt(deltaHeight));
    }
}
