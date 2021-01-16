package net.modificationstation.stationapi.api.common.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryProvider;
import net.modificationstation.stationapi.impl.common.block.HasMetaBlockItemImpl;
import net.modificationstation.stationapi.template.common.item.MetaBlock;

import java.lang.annotation.*;
import java.util.function.IntFunction;

/**
 * Interface that pre-defines block item's factory to be {@link MetaBlock#MetaBlock(int)}
 * @author mine_diver
 * @see BlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory.At
 * @see At
 * @see HasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem.At
 * @see MetaBlock
 */
public interface HasMetaBlockItem extends HasCustomBlockItemFactory {

    /**
     * The logic implementation.
     * @return {@link MetaBlock#MetaBlock(int)}.
     */
    @Override
    default IntFunction<PlaceableTileEntity> getBlockItemFactory() {
        return HasMetaBlockItemImpl.FACTORY;
    }

    /**
     * Annotation alternative of {@link HasMetaBlockItem}.
     * @see BlockItemFactoryProvider
     * @see HasCustomBlockItemFactory
     * @see HasCustomBlockItemFactory.At
     * @see HasMetaBlockItem
     * @see HasMetaNamedBlockItem
     * @see HasMetaNamedBlockItem.At
     * @see MetaBlock
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    @interface At { }
}
