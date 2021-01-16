package net.modificationstation.stationapi.api.common.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryProvider;
import net.modificationstation.stationapi.impl.common.block.HasMetaNamedBlockItemImpl;
import net.modificationstation.stationapi.template.common.item.MetaNamedBlock;

import java.lang.annotation.*;
import java.util.function.IntFunction;

/**
 * Interface that pre-defines block item's factory to be {@link MetaNamedBlock#MetaNamedBlock(int)}
 * @author mine_diver
 * @see BlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory.At
 * @see HasMetaBlockItem
 * @see HasMetaBlockItem.At
 * @see At
 * @see MetaNamedBlock
 */
public interface HasMetaNamedBlockItem extends HasCustomBlockItemFactory {

    /**
     * The logic implementation.
     * @return {@link MetaNamedBlock#MetaNamedBlock(int)}.
     */
    @Override
    default IntFunction<PlaceableTileEntity> getBlockItemFactory() {
        return HasMetaNamedBlockItemImpl.FACTORY;
    }

    /**
     * Annotation alternative of {@link HasMetaNamedBlockItem}.
     * @see BlockItemFactoryProvider
     * @see HasCustomBlockItemFactory
     * @see HasCustomBlockItemFactory.At
     * @see HasMetaBlockItem
     * @see HasMetaBlockItem.At
     * @see HasMetaNamedBlockItem
     * @see MetaNamedBlock
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    @interface At { }
}
