package net.modificationstation.stationapi.api.common.block;

import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationapi.api.common.event.block.BlockItemFactoryCallback;

import java.util.function.IntFunction;

/**
 * Interface that blocks can use to provide a custom block item factory on register via {@link BlockItemFactoryCallback} hook.
 * @author mine_diver
 * @see BlockItemFactoryCallback
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 */
public interface IHasCustomBlockItemFactory {

    /**
     * BlockItem factory supplier method.
     * @return the {@link IntFunction} instance that'll be executed to get block item's instance.
     */
    IntFunction<PlaceableTileEntity> getBlockItemFactory();
}
