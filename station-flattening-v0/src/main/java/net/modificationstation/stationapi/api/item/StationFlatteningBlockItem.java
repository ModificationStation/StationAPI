package net.modificationstation.stationapi.api.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Map;

public interface StationFlatteningBlockItem {

    default BlockBase getBlock() {
        return Util.assertImpl();
    }

    default void appendBlocks(Map<BlockBase, ItemBase> blockItems, ItemBase blockItem) {
        blockItems.put(getBlock(), blockItem);
    }
}
