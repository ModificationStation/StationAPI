package net.modificationstation.stationapi.api.common.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;

public interface EffectiveOnMeta {

    boolean isEffectiveOn(BlockBase tile, int meta, ItemInstance itemInstance);
}
