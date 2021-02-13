package net.modificationstation.stationapi.api.common.block;

import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryCallback;
import net.modificationstation.stationapi.template.common.item.MetaNamedBlock;

import java.lang.annotation.*;

/**
 * Annotation alternative of {@link IHasMetaNamedBlockItem}.
 * @author mine_diver
 * @see BlockItemFactoryCallback
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see MetaNamedBlock
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface HasMetaNamedBlockItem { }
