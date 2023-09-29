package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.maths.MutableBlockPos;

import java.util.function.Function;

public class PositionSurfaceCondition implements SurfaceCondition {
	private final MutableBlockPos pos = new MutableBlockPos(0, 0, 0);
	private final Function<TilePos, Boolean> function;
	
	public PositionSurfaceCondition(Function<TilePos, Boolean> function) {
		this.function = function;
	}
	
	@Override
	public boolean canApply(Level level, int x, int y, int z, BlockState state) {
		pos.set(x, y, z);
		return function.apply(pos);
	}
}
