package net.modificationstation.stationapi.api.common.block;

import net.minecraft.item.ItemInstance;

public interface BlockEffectiveOnMeta {
    float isToolEffectiveOn(int blockMeta, ItemInstance itemInstance);
}
