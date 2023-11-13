package net.modificationstation.stationapi.api.worldgen.surface.condition;

import net.minecraft.class_458;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction.AxisDirection;
import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.util.Random;

public class DepthSurfaceCondition implements SurfaceCondition {
    private static final class_458 NOISE = new class_458(new Random(0), 2);
    private static final double[] BUFFER = new double[1];
    private final AxisDirection direction;
    private final int minDepth;
    private final int maxDepth;

    public DepthSurfaceCondition(int depth, AxisDirection direction) {
        this(depth, depth, direction);
    }

    public DepthSurfaceCondition(int minDepth, int maxDepth, AxisDirection direction) {
        this.direction = direction;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
    }

    @Override
    public boolean canApply(World world, int x, int y, int z, BlockState state) {
        int depth = minDepth;
        if (minDepth != maxDepth) {
            depth = MathHelper.lerp(NOISE.method_1516(BUFFER, x, z, 1, 1, 0.1, 0.1, 0.25)[0], minDepth, maxDepth);
        }
        state = world.getBlockState(x, y - depth * direction.offset(), z);
        return state.isAir() || state.getMaterial().method_893() || state.getMaterial().method_896() || !state.getMaterial().method_907();
    }
}
