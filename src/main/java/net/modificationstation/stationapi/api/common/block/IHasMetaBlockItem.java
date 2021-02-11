package net.modificationstation.stationapi.api.common.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryProvider;
import net.modificationstation.stationapi.impl.common.block.HasMetaBlockItemImpl;
import net.modificationstation.stationapi.template.common.item.MetaBlock;

import java.util.function.IntFunction;

/**
 * Interface that pre-defines block item's factory to be {@link MetaBlock#MetaBlock(int)}
 * @author mine_diver
 * @see BlockItemFactoryProvider
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 * @see MetaBlock
 */
public interface IHasMetaBlockItem extends IHasCustomBlockItemFactory {

    /**
     * The logic implementation.
     * @return {@link MetaBlock#MetaBlock(int)}.
     */
    @Override
    default IntFunction<PlaceableTileEntity> getBlockItemFactory() {
        return HasMetaBlockItemImpl.FACTORY;
    }
}
