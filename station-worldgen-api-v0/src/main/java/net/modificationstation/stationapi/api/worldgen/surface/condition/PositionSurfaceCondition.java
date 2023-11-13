package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;

import java.util.function.Predicate;

public class PositionSurfaceCondition implements SurfaceCondition {
    private final MutableBlockPos pos = new MutableBlockPos(0, 0, 0);
    private final Predicate<BlockPos> predicate;

    public PositionSurfaceCondition(Predicate<BlockPos> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean canApply(World world, int x, int y, int z, BlockState state) {
        pos.set(x, y, z);
        return predicate.test(pos);
    }
}
