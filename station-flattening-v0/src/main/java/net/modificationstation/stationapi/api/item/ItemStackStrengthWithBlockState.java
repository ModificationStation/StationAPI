package net.modificationstation.stationapi.api.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;

public interface ItemStackStrengthWithBlockState {

    boolean isSuitableFor(PlayerEntity player, BlockView blockView, BlockPos blockPos, BlockState state);

    float getMiningSpeedMultiplier(PlayerEntity player, BlockView blockView, BlockPos blockPos, BlockState state);
}
