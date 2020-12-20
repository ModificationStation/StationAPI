package net.modificationstation.stationloader.api.common.block;

import net.minecraft.item.ItemInstance;

public interface BlockEffectiveOnMeta {
    float isToolEffectiveOn(int blockMeta, ItemInstance itemInstance);
}
