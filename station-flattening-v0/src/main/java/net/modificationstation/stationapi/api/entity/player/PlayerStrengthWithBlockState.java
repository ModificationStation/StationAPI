package net.modificationstation.stationapi.api.entity.player;

import net.modificationstation.stationapi.api.block.BlockState;

public interface PlayerStrengthWithBlockState {

    boolean canHarvest(BlockState state);

    float getBlockBreakingSpeed(BlockState state);
}
