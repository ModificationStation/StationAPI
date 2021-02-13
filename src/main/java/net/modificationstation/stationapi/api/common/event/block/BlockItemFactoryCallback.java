package net.modificationstation.stationapi.api.common.event.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.common.block.*;

import java.util.function.IntFunction;

/**
 * When blocks are being registered, this event is called to make it possible to replace the default block item for the current block.
 * @author mine_diver
 * @see IHasCustomBlockItemFactory
 * @see HasCustomBlockItemFactory
 * @see IHasMetaBlockItem
 * @see HasMetaBlockItem
 * @see IHasMetaNamedBlockItem
 * @see HasMetaNamedBlockItem
 */
public class BlockItemFactoryCallback extends BlockEvent {

    /**
     * Current factory that's going to be executed to get block item instance.
     */
    public IntFunction<Block> currentFactory;

    public BlockItemFactoryCallback(BlockBase block, IntFunction<Block> currentFactory) {
        super(block);
        this.currentFactory = currentFactory;
    }
}
