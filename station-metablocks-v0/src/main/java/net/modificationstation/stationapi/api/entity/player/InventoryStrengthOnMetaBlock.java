package net.modificationstation.stationapi.api.entity.player;

import net.minecraft.block.BlockBase;

public interface InventoryStrengthOnMetaBlock {

    float getStrengthOnBlock(BlockBase arg, int meta);
}
