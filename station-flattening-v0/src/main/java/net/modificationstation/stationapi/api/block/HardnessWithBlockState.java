package net.modificationstation.stationapi.api.block;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.TilePos;

public interface HardnessWithBlockState {

    float getHardness(BlockState state, BlockView blockView, TilePos pos);

    float calcBlockBreakingDelta(BlockState state, PlayerBase player, BlockView world, TilePos pos);
}
