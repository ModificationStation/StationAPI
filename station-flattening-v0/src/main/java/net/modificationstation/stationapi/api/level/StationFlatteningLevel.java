package net.modificationstation.stationapi.api.level;

import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningLevel extends BlockStateView, HeightLimitView {

    @Override
    default BlockState getBlockState(int x, int y, int z) {
        return Util.assertImpl();
    }

    default BlockState setBlockState(int x, int y, int z, BlockState blockState) {
        return Util.assertImpl();
    }

    default BlockState setBlockStateWithNotify(int x, int y, int z, BlockState blockState) {
        return Util.assertImpl();
    }

    default BlockState setBlockState(TilePos pos, BlockState blockState) {
        return setBlockState(pos.x, pos.y, pos.z, blockState);
    }

    default BlockState setBlockStateWithNotify(TilePos pos, BlockState blockState) {
        return setBlockStateWithNotify(pos.x, pos.y, pos.z, blockState);
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
