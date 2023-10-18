package net.modificationstation.stationapi.api.worldgen.structure;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.util.Random;

public class WeightedStructure extends Structure {
	private final Structure structure;
	private final int weight;
	
	public WeightedStructure(Structure structure, int weight) {
		this.structure = structure;
		this.weight = weight;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (random.nextInt(weight) > 0) return false;
		return structure.generate(level, random, x, y, z);
	}
}
