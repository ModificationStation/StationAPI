package net.modificationstation.stationapi.api.world;

import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;

public interface BlockStateView {

    BlockState getBlockState(int x, int y, int z);

    default BlockState getBlockState(TilePos pos) {
        return getBlockState(pos.getX(), pos.getY(), pos.getZ());
    }
}
