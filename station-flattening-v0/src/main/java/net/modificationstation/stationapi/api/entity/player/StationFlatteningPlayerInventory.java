package net.modificationstation.stationapi.api.entity.player;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Util;

public interface StationFlatteningPlayerInventory extends PlayerStrengthWithBlockState {

    @Override
    default boolean canHarvest(BlockView blockView, BlockPos blockPos, BlockState state) {
        return Util.assertImpl();
    }

    @Override
    default float getBlockBreakingSpeed(BlockView blockView, BlockPos blockPos, BlockState state) {
        return Util.assertImpl();
    }
}
