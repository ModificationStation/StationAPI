package net.modificationstation.stationapi.api.common.block;

import net.modificationstation.stationapi.api.common.event.block.BlockEvent;
import net.modificationstation.stationapi.template.common.item.MetaBlock;

import java.lang.annotation.*;

/**
 * Annotation alternative of {@link IHasMetaBlockItem}.
 * @author mine_diver
 * @see BlockEvent.ItemFactory
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 * @see MetaBlock
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface HasMetaBlockItem { }
