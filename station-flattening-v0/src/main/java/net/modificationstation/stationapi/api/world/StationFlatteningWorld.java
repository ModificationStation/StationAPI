package net.modificationstation.stationapi.api.world;

import net.minecraft.util.math.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningWorld extends BlockStateView, HeightLimitView {

    @Override
    default BlockState getBlockState(int x, int y, int z) {
        return Util.assertImpl();
    }

    /**
     * @see net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk#setBlockState(int, int, int, BlockState)
     */
    default BlockState setBlockStateWithoutNotifyingNeighbors(int x, int y, int z, BlockState blockState) {
        return Util.assertImpl();
    }

    /**
     * @see net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk#setBlockState(int, int, int, BlockState)
     */
    default BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return Util.assertImpl();
    }

    /**
     * @see net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk#setBlockState(int, int, int, BlockState, int)
     */
    default BlockState setBlockStateWithoutNotifyingNeighbors(int x, int y, int z, BlockState blockState, int meta) {
        return Util.assertImpl();
    }

    default BlockState setBlockStateWithoutNotifyingNeighbors(BlockPos pos, BlockState blockState) {
        return setBlockStateWithoutNotifyingNeighbors(pos.x, pos.y, pos.z, blockState);
    }

    default BlockState setBlockState(BlockPos pos, BlockState blockState) {
        return setBlockState(pos.x, pos.y, pos.z, blockState);
    }

    /**
     * @see net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk#setBlockState(int, int, int, BlockState, int)
     */
    default BlockState setBlockState(int x, int y, int z, BlockState blockState, int meta) {
        return Util.assertImpl();
    }

    /**
     * @see net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk#setBlock(int, int, int, BlockState, int)
     */
    default BlockState setBlockWithoutNotifyingNeighbors(int x, int y, int z, BlockState blockState, int meta) {
        return Util.assertImpl();
    }

    /**
     * @see net.modificationstation.stationapi.api.world.chunk.StationFlatteningChunk#setBlock(int, int, int, BlockState, int)
     */
    default BlockState setBlock(int x, int y, int z, BlockState blockState, int meta) {
        return Util.assertImpl();
    }

    @Override
    default int getHeight() {
        return Util.assertImpl();
    }

    @Override
    default int getBottomY() {
        return Util.assertImpl();
    }
}
