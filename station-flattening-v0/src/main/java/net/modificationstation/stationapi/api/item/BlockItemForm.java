package net.modificationstation.stationapi.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

import java.util.Map;
import java.util.function.Supplier;

public interface BlockItemForm {

    static BlockItem of(Block block) {
        return of(() -> new BlockItem(ItemRegistry.SHIFTED_ID.get(-1)), block);
    }

    static <T extends BlockItem> T of(Supplier<T> blockItemFactory, Block block) {
        T blockItem = blockItemFactory.get();
        blockItem.setBlock(block);
        return blockItem;
    }

    Block getBlock();

    default void appendBlocks(Map<Block, Item> blockItems, Item blockItem) {
        blockItems.put(getBlock(), blockItem);
    }
}
