package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

public class StateSurfaceCondition implements SurfaceCondition {
    private final BlockState state;

    public StateSurfaceCondition(BlockState state) {
        this.state = state;
    }

    @Override
    public boolean canApply(World level, int x, int y, int z, BlockState state) {
        return state == this.state;
    }
}
