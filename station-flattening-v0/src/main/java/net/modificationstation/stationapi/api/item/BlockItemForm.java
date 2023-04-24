package net.modificationstation.stationapi.api.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.registry.ItemRegistry;

import java.util.Map;
import java.util.function.Supplier;

public interface BlockItemForm {

    static Block of(BlockBase block) {
        return of(() -> new Block(ItemRegistry.SHIFTED_ID.get(-1)), block);
    }

    static <T extends Block> T of(Supplier<T> blockItemFactory, BlockBase block) {
        T blockItem = blockItemFactory.get();
        blockItem.setBlock(block);
        return blockItem;
    }

    BlockBase getBlock();

    default void appendBlocks(Map<BlockBase, ItemBase> blockItems, ItemBase blockItem) {
        blockItems.put(getBlock(), blockItem);
    }
}
