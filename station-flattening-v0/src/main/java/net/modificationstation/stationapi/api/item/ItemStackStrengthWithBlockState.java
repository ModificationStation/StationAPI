package net.modificationstation.stationapi.api.item;

import net.modificationstation.stationapi.api.block.BlockState;

public interface ItemStackStrengthWithBlockState {

    boolean isSuitableFor(BlockState state);

    float getMiningSpeedMultiplier(BlockState state);
}
