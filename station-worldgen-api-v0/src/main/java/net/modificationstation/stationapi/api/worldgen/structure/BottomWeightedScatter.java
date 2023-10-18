package net.modificationstation.stationapi.api.worldgen.structure;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.util.Random;

public class BottomWeightedScatter extends VolumetricScatterStructure {
	public BottomWeightedScatter(Structure structure, int iterations, int minHeight, int maxHeight) {
		super(structure, iterations, minHeight, maxHeight);
	}
	
	@Override
	int getHeight(Level level, Random random, int x, int z) {
		return random.nextInt(minHeight + random.nextInt(deltaHeight));
	}
}
