package net.modificationstation.stationapi.api.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;

public interface StrengthOnMeta {

    float getStrengthOnBlock(ItemInstance item, BlockBase tile, int meta);
}
