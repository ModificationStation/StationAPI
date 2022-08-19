package net.modificationstation.stationapi.api.entity.player;

import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningPlayerBase extends PlayerStrengthWithBlockState {

    @Override
    default boolean canHarvest(BlockState state) {
        return Util.assertImpl();
    }

    @Override
    default float getBlockBreakingSpeed(BlockState state) {
        return Util.assertImpl();
    }
}
