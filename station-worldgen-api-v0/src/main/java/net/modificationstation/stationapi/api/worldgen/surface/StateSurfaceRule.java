package net.modificationstation.stationapi.api.worldgen.surface;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;

public class StateSurfaceRule extends SurfaceRule {
    private final BlockState state;

    public StateSurfaceRule(BlockState state) {
        this.state = state;
    }

    @Override
    public void apply(World level, int x, int y, int z) {
        level.setBlockState(x, y, z, state);
    }
}
