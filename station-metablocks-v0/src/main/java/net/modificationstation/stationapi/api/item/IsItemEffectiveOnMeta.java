package net.modificationstation.stationapi.api.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;

public interface IsItemEffectiveOnMeta {

    boolean isEffectiveOn(ItemInstance itemInstance, BlockBase tile, int meta);
}
