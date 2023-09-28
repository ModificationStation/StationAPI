package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction.AxisDirection;

public class DepthSurfaceCondition implements SurfaceCondition {
	private final AxisDirection direction;
	private final int depth;
	
	public DepthSurfaceCondition(int depth, AxisDirection direction) {
		this.direction = direction;
		this.depth = depth;
	}
	
	@Override
	public boolean canApply(Level level, int x, int y, int z, BlockState state) {
		state = level.getBlockState(x, y - depth * direction.offset(), z);
		return state.isAir() || state.getMaterial().isLiquid() || state.getMaterial().isReplaceable() || !state.getMaterial().blocksMovement();
	}
}
