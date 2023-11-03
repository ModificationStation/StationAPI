package net.modificationstation.stationapi.api.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public interface HardnessWithBlockState {

    float getHardness(BlockState state, BlockView blockView, BlockPos pos);

    float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos);
}
