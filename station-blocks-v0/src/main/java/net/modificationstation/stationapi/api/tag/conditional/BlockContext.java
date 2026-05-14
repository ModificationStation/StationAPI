package net.modificationstation.stationapi.api.tag.conditional;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.util.context.Context;
import org.jetbrains.annotations.Nullable;

public final class BlockContext implements Context {
    private static final ThreadLocal<BlockContext> INSTANCE = ThreadLocal.withInitial(BlockContext::new);

    public static BlockContext of(BlockView world, BlockPos pos) {
        return of(world, pos.x, pos.y, pos.z);
    }

    public static BlockContext of(BlockView world, int x, int y, int z) {
        BlockContext ctx = INSTANCE.get();
        ctx.metadata = world.getBlockMeta(x, y, z);
        return ctx;
    }

    private Integer metadata;

    private BlockContext() {}

    @Override
    public <VALUE> @Nullable VALUE get(Key<VALUE> key) {
        if (key == BlockTagConditions.BLOCK_METADATA) {
            //noinspection unchecked
            return (VALUE) metadata;
        }
        return null;
    }
}
