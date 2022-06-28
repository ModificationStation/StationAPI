package net.modificationstation.stationapi.api.block;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;

public interface AfterBreakWithBlockState {

    void afterBreak(Level level, PlayerBase player, int x, int y, int z, BlockState state, int meta);
}
