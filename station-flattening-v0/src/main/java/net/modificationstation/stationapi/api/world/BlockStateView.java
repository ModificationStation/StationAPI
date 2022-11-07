package net.modificationstation.stationapi.api.world;

import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.BlockPos;

public interface BlockStateView {

    BlockState getBlockState(int x, int y, int z);

    @Deprecated
    default BlockState getBlockState(TilePos pos) {
        return getBlockState(pos.x, pos.y, pos.z);
    }

    default BlockState getBlockState(BlockPos pos) {
        return getBlockState(pos.getX(), pos.getY(), pos.getZ());
    }
}
