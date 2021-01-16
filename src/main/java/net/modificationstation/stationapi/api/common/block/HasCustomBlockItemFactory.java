package net.modificationstation.stationapi.api.common.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryProvider;

import java.lang.annotation.*;
import java.util.function.IntFunction;

/**
 * Interface that blocks can use to provide a custom block item factory on register via {@link BlockItemFactoryProvider} hook.
 * @author mine_diver
 * @see BlockItemFactoryProvider
 * @see At
 * @see HasMetaBlockItem
 * @see HasMetaBlockItem.At
 * @see HasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem.At
 */
public interface HasCustomBlockItemFactory {

    /**
     * BlockItem factory supplier method.
     * @return the {@link IntFunction} instance that'll be executed to get block item's instance.
     */
    IntFunction<PlaceableTileEntity> getBlockItemFactory();

    /**
     * Annotation alternative of {@link HasCustomBlockItemFactory}.
     * @see BlockItemFactoryProvider
     * @see HasCustomBlockItemFactory
     * @see HasMetaBlockItem
     * @see HasMetaBlockItem.At
     * @see HasMetaNamedBlockItem
     * @see HasMetaNamedBlockItem.At
     */
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    @interface At {

        /**
         * BlockItem class supplier method.
         * @return the block item class that'll be instantiated and used as current block's item.
         */
        Class<? extends PlaceableTileEntity> value();
    }
}
