package net.modificationstation.stationapi.impl.common.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.block.*;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryCallback;
import net.modificationstation.stationapi.template.common.item.MetaNamedBlock;

import java.util.function.IntFunction;

/**
 * {@link IHasMetaNamedBlockItem} implementation class.
 * @author mine_diver
 * @see BlockItemFactoryCallback
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 */
public class HasMetaNamedBlockItemImpl implements BlockItemFactoryCallback {

    /**
     * Handles block's {@link HasMetaNamedBlockItem} annotation if it's present via {@link BlockItemFactoryCallback} hook.
     * @param block current block.
     * @param currentFactory current factory that's going to be executed to get block item instance.
     * @return {@link MetaNamedBlock#MetaNamedBlock(int)} if annotation is present, otherwise currentFactory.
     */
    @Override
    public IntFunction<PlaceableTileEntity> getBlockItemFactory(BlockBase block, IntFunction<PlaceableTileEntity> currentFactory) {
        if (block.getClass().isAnnotationPresent(HasMetaNamedBlockItem.class))
            currentFactory = FACTORY;
        return currentFactory;
    }

    /**
     * {@link MetaNamedBlock#MetaNamedBlock(int)} field.
     */
    public static final IntFunction<PlaceableTileEntity> FACTORY = MetaNamedBlock::new;
}
