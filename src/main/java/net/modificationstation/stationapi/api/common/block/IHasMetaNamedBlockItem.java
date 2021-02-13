package net.modificationstation.stationapi.api.common.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryCallback;
import net.modificationstation.stationapi.impl.common.block.HasMetaNamedBlockItemImpl;
import net.modificationstation.stationapi.template.common.item.MetaNamedBlock;

import java.util.function.IntFunction;

/**
 * Interface that pre-defines block item's factory to be {@link MetaNamedBlock#MetaNamedBlock(int)}
 * @author mine_diver
 * @see BlockItemFactoryCallback
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see HasMetaNamedBlockItem
 * @see MetaNamedBlock
 */
public interface IHasMetaNamedBlockItem extends IHasCustomBlockItemFactory {

    /**
     * The logic implementation.
     * @return {@link MetaNamedBlock#MetaNamedBlock(int)}.
     */
    @Override
    default IntFunction<PlaceableTileEntity> getBlockItemFactory() {
        return HasMetaNamedBlockItemImpl.FACTORY;
    }

}
