package net.modificationstation.stationapi.impl.common.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.common.block.*;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryCallback;
import net.modificationstation.stationapi.template.common.item.MetaBlock;

import java.util.function.IntFunction;

/**
 * {@link IHasMetaBlockItem} implementation class.
 * @author mine_diver
 * @see BlockItemFactoryCallback
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 */
public class HasMetaBlockItemImpl implements BlockItemFactoryCallback {

    /**
     * Handles block's {@link HasMetaBlockItem} annotation if it's present via {@link BlockItemFactoryCallback} hook.
     * @param block current block.
     * @param currentFactory current factory that's going to be executed to get block item instance.
     * @return {@link MetaBlock#MetaBlock(int)} if annotation is present, otherwise currentFactory.
     */
    @Override
    public IntFunction<Block> getBlockItemFactory(BlockBase block, IntFunction<Block> currentFactory) {
        if (block.getClass().isAnnotationPresent(HasMetaBlockItem.class))
            currentFactory = FACTORY;
        return currentFactory;
    }

    /**
     * {@link MetaBlock#MetaBlock(int)} field.
     */
    public static final IntFunction<Block> FACTORY = MetaBlock::new;
}
