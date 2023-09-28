package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;

public class BlockSurfaceCondition implements SurfaceCondition {
	private final BlockBase block;
	
	public BlockSurfaceCondition(BlockBase block) {
		this.block = block;
	}
	
	@Override
	public boolean canApply(Level level, int x, int y, int z, BlockState state) {
		return state.isOf(block);
	}
}
