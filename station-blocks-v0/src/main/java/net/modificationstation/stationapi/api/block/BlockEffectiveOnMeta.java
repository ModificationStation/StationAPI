package net.modificationstation.stationapi.api.block;

import net.minecraft.item.ItemInstance;

public interface BlockEffectiveOnMeta {
    float isToolEffectiveOn(int blockMeta, ItemInstance itemInstance);
}
