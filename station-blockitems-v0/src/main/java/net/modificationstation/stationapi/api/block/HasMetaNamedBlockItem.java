package net.modificationstation.stationapi.api.block;

import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.template.item.MetaNamedBlock;

import java.lang.annotation.*;

/**
 * Annotation alternative of {@link MetaNamedBlockItemProvider}.
 * @author mine_diver
 * @see BlockItemFactoryEvent
 * @see CustomBlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see MetaBlockItemProvider
 * @see HasMetaBlockItem
 * @see MetaNamedBlockItemProvider
 * @see MetaNamedBlock
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface HasMetaNamedBlockItem { }
