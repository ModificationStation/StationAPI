package net.modificationstation.stationapi.api.world;

import net.minecraft.util.math.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;

public interface BlockStateView {

    BlockState getBlockState(int x, int y, int z);

    default BlockState getBlockState(BlockPos pos) {
        return getBlockState(pos.getX(), pos.getY(), pos.getZ());
    }
}
