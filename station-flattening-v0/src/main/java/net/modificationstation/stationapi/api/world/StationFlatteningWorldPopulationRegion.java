package net.modificationstation.stationapi.api.world;

import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningWorldPopulationRegion extends BlockStateView {

    @Override
    default BlockState getBlockState(int x, int y, int z) {
        return Util.assertImpl();
    }
}
