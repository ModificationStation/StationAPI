package net.modificationstation.stationapi.api.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;

public interface IsItemInstanceEffectiveOnMeta {

    static IsItemInstanceEffectiveOnMeta cast(ItemInstance itemInstance) {
        return IsItemInstanceEffectiveOnMeta.class.cast(itemInstance);
    }

    boolean isEffectiveOn(BlockBase tile, int meta);
}
