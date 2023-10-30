package net.modificationstation.stationapi.api.bonemeal;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.collection.WeightedList;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.Map;
import java.util.Random;

public class BonemealAPI {
	private static final Map<BlockState, WeightedList<Structure>> PLACERS = new Reference2ObjectOpenHashMap<>();
	
	public static void addPlant(BlockState ground, BlockState plant, int weight) {
		addPlant(ground, new SimpleStateStructure(plant), weight);
	}
	
	public static void addPlant(BlockState ground, Structure plant, int weight) {
		PLACERS.computeIfAbsent(ground, g -> new WeightedList<>()).add(plant, weight);
	}
	
	public static void addPlant(TagKey<BlockBase> ground, BlockState plant, int weight) {
		BlockRegistry.INSTANCE.forEach(blockBase ->
			blockBase.getStateManager().getStates().stream().filter(state -> state.isIn(ground)).forEach(state -> addPlant(state, plant, weight))
		);
	}
	
	public static void addPlant(TagKey<BlockBase> ground, Structure plant, int weight) {
		BlockRegistry.INSTANCE.forEach(blockBase ->
			blockBase.getStateManager().getStates().stream().filter(state -> state.isIn(ground)).forEach(state -> addPlant(state, plant, weight))
		);
	}
	
	public static boolean generate(Level level, int x, int y, int z, BlockState state, int side) {
		WeightedList<Structure> structures = PLACERS.get(state);
		if (structures == null) return false;
		Random random = level.rand;
		Direction offset = Direction.byId(side);
		structures.get(random).generate(
			level,
			random,
			x + offset.getOffsetX(),
			y + offset.getOffsetY(),
			z + offset.getOffsetZ()
		);
		for (byte i = 0; i < 127; i++) {
			int px = x + random.nextInt(7) - 3;
			int py = y + random.nextInt(5) - 2;
			int pz = z + random.nextInt(7) - 3;
			state = level.getBlockState(px, py, pz);
			structures = PLACERS.get(state);
			if (structures == null) continue;
			structures.get(random).generate(level, random, px, py + 1, pz);
		}
		return true;
	}
	
	private static class SimpleStateStructure extends Structure {
		private final BlockState state;
		
		private SimpleStateStructure(BlockState state) {
			this.state = state;
		}
		
		@Override
		public boolean generate(Level level, Random random, int x, int y, int z) {
			BlockState levelState = level.getBlockState(x, y, z);
			if (!levelState.isAir() && !levelState.getMaterial().isLiquid()) return false;
			if (state.getBlock().canPlaceAt(level, x, y, z)) {
				level.setBlockState(x, y, z, state);
				return true;
			}
			return false;
		}
	}
	
	private static class GrassStructure extends Structure {
		private static final BlockState STATE = BlockBase.TALLGRASS.getDefaultState();
		
		@Override
		public boolean generate(Level level, Random random, int x, int y, int z) {
			BlockState levelState = level.getBlockState(x, y, z);
			if (!levelState.isAir() && !levelState.getMaterial().isLiquid()) return false;
			if (STATE.getBlock().canPlaceAt(level, x, y, z)) {
				level.setBlockState(x, y, z, STATE);
				level.setTileMeta(x, y, z, 1);
				return true;
			}
			return false;
		}
	}
	
	static {
		addPlant(BlockBase.GRASS.getDefaultState(), new GrassStructure(), 1);
	}
}
