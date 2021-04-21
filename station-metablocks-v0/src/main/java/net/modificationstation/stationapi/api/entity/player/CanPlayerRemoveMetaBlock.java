package net.modificationstation.stationapi.api.entity.player;

import net.minecraft.block.BlockBase;

public interface CanPlayerRemoveMetaBlock {

    boolean canRemoveBlock(BlockBase block, int meta);
}
