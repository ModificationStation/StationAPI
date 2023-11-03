package net.modificationstation.stationapi.api.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface AfterBreakWithBlockState {

    void afterBreak(World level, PlayerEntity player, int x, int y, int z, BlockState state, int meta);
}
