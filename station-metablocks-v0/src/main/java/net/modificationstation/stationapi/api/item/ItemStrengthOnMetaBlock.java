package net.modificationstation.stationapi.api.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;

public interface ItemStrengthOnMetaBlock {

    float getStrengthOnBlock(ItemInstance item, BlockBase tile, int meta);
}
