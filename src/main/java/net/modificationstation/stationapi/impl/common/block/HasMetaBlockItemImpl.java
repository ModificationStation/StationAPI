package net.modificationstation.stationapi.impl.common.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.common.block.HasMetaBlockItem;
import net.modificationstation.stationapi.api.common.block.HasMetaNamedBlockItem;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryProvider;
import net.modificationstation.stationapi.template.common.item.MetaBlock;

import java.util.function.IntFunction;

/**
 * {@link HasMetaBlockItem} implementation class.
 * @author mine_diver
 * @see BlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory.At
 * @see HasMetaBlockItem
 * @see HasMetaBlockItem.At
 * @see HasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem.At
 */
public class HasMetaBlockItemImpl implements BlockItemFactoryProvider {

    /**
     * Handles block's {@link HasMetaBlockItem.At} annotation if it's present via {@link BlockItemFactoryProvider} hook.
     * @param block current block.
     * @param currentFactory current factory that's going to be executed to get block item instance.
     * @return {@link MetaBlock#MetaBlock(int)} if annotation is present, otherwise currentFactory.
     */
    @Override
    public IntFunction<PlaceableTileEntity> getBlockItemFactory(BlockBase block, IntFunction<PlaceableTileEntity> currentFactory) {
        if (block.getClass().isAnnotationPresent(HasMetaBlockItem.At.class))
            currentFactory = FACTORY;
        return currentFactory;
    }

    /**
     * {@link MetaBlock#MetaBlock(int)} field.
     */
    public static final IntFunction<PlaceableTileEntity> FACTORY = MetaBlock::new;
}
