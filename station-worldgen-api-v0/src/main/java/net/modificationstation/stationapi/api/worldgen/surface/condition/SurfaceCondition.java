package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;

@FunctionalInterface
public interface SurfaceCondition {
	boolean canApply(Level level, int x, int y, int z, BlockState state);
}
