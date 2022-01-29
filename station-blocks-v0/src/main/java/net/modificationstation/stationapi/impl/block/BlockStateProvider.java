package net.modificationstation.stationapi.impl.block;

public interface BlockStateProvider {

    BlockState getBlockState(int x, int y, int z);
}
