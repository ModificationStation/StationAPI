package net.modificationstation.stationapi.impl.common.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.common.block.HasMetaBlockItem;
import net.modificationstation.stationapi.api.common.block.HasMetaNamedBlockItem;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryProvider;
import net.modificationstation.stationapi.template.common.item.MetaNamedBlock;

import java.util.function.IntFunction;

/**
 * {@link HasMetaNamedBlockItem} implementation class.
 * @author mine_diver
 * @see BlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory.At
 * @see HasMetaBlockItem
 * @see HasMetaBlockItem.At
 * @see HasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem.At
 */
public class HasMetaNamedBlockItemImpl implements BlockItemFactoryProvider {

    /**
     * Handles block's {@link HasMetaNamedBlockItem.At} annotation if it's present via {@link BlockItemFactoryProvider} hook.
     * @param block current block.
     * @param currentFactory current factory that's going to be executed to get block item instance.
     * @return {@link MetaNamedBlock#MetaNamedBlock(int)} if annotation is present, otherwise currentFactory.
     */
    @Override
    public IntFunction<PlaceableTileEntity> getBlockItemFactory(BlockBase block, IntFunction<PlaceableTileEntity> currentFactory) {
        if (block.getClass().isAnnotationPresent(HasMetaNamedBlockItem.At.class))
            currentFactory = FACTORY;
        return currentFactory;
    }

    /**
     * {@link MetaNamedBlock#MetaNamedBlock(int)} field.
     */
    public static final IntFunction<PlaceableTileEntity> FACTORY = MetaNamedBlock::new;
}
