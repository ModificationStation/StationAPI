package net.modificationstation.stationapi.api.block;

import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.template.item.MetaBlock;
import net.modificationstation.stationapi.impl.block.HasMetaBlockItemImpl;

import java.util.function.IntFunction;

/**
 * Interface that pre-defines block item's factory to be {@link MetaBlock#MetaBlock(int)}
 * @author mine_diver
 * @see BlockItemFactoryEvent
 * @see CustomBlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see HasMetaBlockItem
 * @see MetaNamedBlockItemProvider
 * @see HasMetaNamedBlockItem
 * @see MetaBlock
 */
public interface MetaBlockItemProvider extends CustomBlockItemFactoryProvider {

    /**
     * The logic implementation.
     * @return {@link MetaBlock#MetaBlock(int)}.
     */
    @Override
    default IntFunction<Block> getBlockItemFactory() {
        return HasMetaBlockItemImpl.FACTORY;
    }
}
