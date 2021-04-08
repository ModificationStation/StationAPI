package net.modificationstation.stationapi.api.block;

import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.common.event.block.BlockEvent;

import java.util.function.*;

/**
 * Interface that blocks can use to provide a custom block item factory on register via {@link BlockEvent.ItemFactory} hook.
 * @author mine_diver
 * @see BlockEvent.ItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 */
public interface IHasCustomBlockItemFactory {

    /**
     * BlockItem factory supplier method.
     * @return the {@link IntFunction} instance that'll be executed to get block item's instance.
     */
    IntFunction<Block> getBlockItemFactory();
}
