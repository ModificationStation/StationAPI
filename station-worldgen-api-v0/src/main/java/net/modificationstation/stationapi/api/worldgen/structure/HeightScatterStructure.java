package net.modificationstation.stationapi.api.worldgen.structure;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.util.Random;

public class HeightScatterStructure extends ScatterStructure {
	public HeightScatterStructure(Structure structure, int iterations) {
		super(structure, iterations);
	}
	
	@Override
	protected int getHeight(Level level, Random random, int x, int y, int z) {
		return level.getHeight(x, z);
	}
}
