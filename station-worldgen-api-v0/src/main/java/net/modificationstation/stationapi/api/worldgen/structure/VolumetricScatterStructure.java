package net.modificationstation.stationapi.api.worldgen.structure;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;

import java.util.Random;

public class VolumetricScatterStructure extends ScatterStructure {
	protected final int minHeight;
	protected final int deltaHeight;
	
	public VolumetricScatterStructure(Structure structure, int iterations, int maxHeight) {
		this(structure, iterations, 0, maxHeight);
	}
	
	public VolumetricScatterStructure(Structure structure, int iterations, int minHeight, int maxHeight) {
		super(structure, iterations);
		this.minHeight = minHeight;
		this.deltaHeight = maxHeight - minHeight + 1;
	}
	
	@Override
	int getHeight(Level level, Random random, int x, int z) {
		return minHeight + random.nextInt(deltaHeight);
	}
}
