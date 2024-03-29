package net.modificationstation.stationapi.api.world.chunk;

import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.world.BlockStateView;

public interface StationFlatteningChunk extends BlockStateView {

    @Override
    default BlockState getBlockState(int x, int y, int z) {
        return Util.assertImpl();
    }

    default BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return Util.assertImpl();
    }

    default BlockState setBlockStateWithMetadata(int x, int y, int z, BlockState blockState, int meta) {
        return Util.assertImpl();
    }
}
