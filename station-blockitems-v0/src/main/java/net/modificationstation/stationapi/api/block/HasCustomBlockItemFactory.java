package net.modificationstation.stationapi.api.block;

import net.minecraft.item.BlockItem;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;

import java.lang.annotation.*;

/**
 * Annotation alternative of {@link CustomBlockItemFactoryProvider}.
 * @author mine_diver
 * @see BlockItemFactoryEvent
 * @see CustomBlockItemFactoryProvider
 * @see MetaBlockItemProvider
 * @see HasMetaBlockItem
 * @see MetaNamedBlockItemProvider
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
    Class<? extends BlockItem> value();
}
