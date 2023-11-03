package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

@FunctionalInterface
public interface SurfaceCondition {
    boolean canApply(World level, int x, int y, int z, BlockState state);
}
