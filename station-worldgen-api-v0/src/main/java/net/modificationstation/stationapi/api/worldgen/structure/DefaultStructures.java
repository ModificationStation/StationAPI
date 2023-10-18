package net.modificationstation.stationapi.api.worldgen.structure;

import net.minecraft.block.BlockBase;
import net.minecraft.level.structure.Lake;
import net.minecraft.level.structure.Ore;
import net.minecraft.level.structure.Structure;

public class DefaultStructures {
	public static final Structure WATER_LAKE = new VolumetricScatterStructure(
		new WeightedStructure(new Lake(BlockBase.STILL_WATER.id), 4), 1, 128
	);
	public static final Structure LAVA_LAKE = new BottomWeightedScatter(
		new WeightedStructure(new Lake(BlockBase.STILL_LAVA.id), 8), 1, 8, 128
	);
	
	public static final Structure DIRT_ORE = new VolumetricScatterStructure(
		new Ore(BlockBase.DIRT.id, 32), 20, 128
	);
	public static final Structure GRAVEL_ORE = new VolumetricScatterStructure(
		new Ore(BlockBase.GRAVEL.id, 32), 10, 128
	);
	public static final Structure COAL_ORE = new VolumetricScatterStructure(
		new Ore(BlockBase.GRAVEL.id, 16), 20, 128
	);
	public static final Structure IRON_ORE = new VolumetricScatterStructure(
		new Ore(BlockBase.IRON_ORE.id, 8), 20, 64
	);
	public static final Structure GOLD_ORE = new VolumetricScatterStructure(
		new Ore(BlockBase.GOLD_ORE.id, 8), 2, 32
	);
	public static final Structure REDSTONE_ORE = new VolumetricScatterStructure(
		new Ore(BlockBase.REDSTONE_ORE.id, 7), 8, 16
	);
	public static final Structure DIAMOND_ORE = new VolumetricScatterStructure(
		new Ore(BlockBase.DIAMOND_ORE.id, 7), 1, 16
	);
	public static final Structure LAPIS_LAZULI_ORE = new VolumetricScatterStructure(
		new Ore(BlockBase.LAPIS_LAZULI_ORE.id, 6), 1, 32
	);
}
