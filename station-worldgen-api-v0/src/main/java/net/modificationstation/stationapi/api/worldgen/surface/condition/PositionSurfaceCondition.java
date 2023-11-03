package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;

import java.util.function.Function;

public class PositionSurfaceCondition implements SurfaceCondition {
    private final MutableBlockPos pos = new MutableBlockPos(0, 0, 0);
    private final Function<BlockPos, Boolean> function;

    public PositionSurfaceCondition(Function<BlockPos, Boolean> function) {
        this.function = function;
    }

    @Override
    public boolean canApply(World level, int x, int y, int z, BlockState state) {
        pos.set(x, y, z);
        return function.apply(pos);
    }
}
