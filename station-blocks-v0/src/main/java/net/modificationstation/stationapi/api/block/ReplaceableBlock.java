package net.modificationstation.stationapi.api.block;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;

public interface ReplaceableBlock {

    boolean canBeReplaced(Level level, int x, int y, int z, BlockBase replacedBy, int replacedByMeta);
}
