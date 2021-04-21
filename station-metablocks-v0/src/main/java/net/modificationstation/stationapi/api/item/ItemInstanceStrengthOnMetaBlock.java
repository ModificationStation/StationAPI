package net.modificationstation.stationapi.api.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;

public interface ItemInstanceStrengthOnMetaBlock {

    static ItemInstanceStrengthOnMetaBlock cast(ItemInstance itemInstance) {
        return ItemInstanceStrengthOnMetaBlock.class.cast(itemInstance);
    }

    float getStrengthOnBlock(BlockBase block, int meta);
}
