package net.modificationstation.stationapi.api.common.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryCallback;

import java.lang.annotation.*;

/**
 * Annotation alternative of {@link IHasCustomBlockItemFactory}.
 * @author mine_diver
 * @see BlockItemFactoryCallback
 * @see IHasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface HasCustomBlockItemFactory {

    /**
     * BlockItem class supplier method.
     * @return the block item class that'll be instantiated and used as current block's item.
     */
    Class<? extends PlaceableTileEntity> value();
}
