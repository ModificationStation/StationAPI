package net.modificationstation.stationapi.api.level;

import net.modificationstation.stationapi.api.block.BlockState;

public interface BlockStateView {

    BlockState getBlockState(int x, int y, int z);

    BlockState setBlockState(int x, int y, int z, BlockState blockState);
}
