package net.modificationstation.stationapi.api.block;

public interface BlockStateView {

    BlockState getBlockState(int x, int y, int z);

    BlockState setBlockState(int x, int y, int z, BlockState blockState);
}
