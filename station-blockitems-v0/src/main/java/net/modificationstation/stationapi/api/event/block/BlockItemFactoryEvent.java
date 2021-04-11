package net.modificationstation.stationapi.api.event.block;

import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.modificationstation.stationapi.api.block.CustomBlockItemFactoryProvider;
import net.modificationstation.stationapi.api.block.MetaBlockItemProvider;
import net.modificationstation.stationapi.api.block.MetaNamedBlockItemProvider;
import net.modificationstation.stationapi.api.block.HasCustomBlockItemFactory;
import net.modificationstation.stationapi.api.block.HasMetaBlockItem;
import net.modificationstation.stationapi.api.block.HasMetaNamedBlockItem;

import java.util.function.*;

/**
 * When blocks are being registered, this event is called to make it possible to replace the default block item for the current block.
 * @author mine_diver
 * @see CustomBlockItemFactoryProvider
 * @see HasCustomBlockItemFactory
 * @see MetaBlockItemProvider
 * @see HasMetaBlockItem
 * @see MetaNamedBlockItemProvider
 * @see HasMetaNamedBlockItem
 */
public class BlockItemFactoryEvent extends BlockEvent {

    /**
     * Current factory that's going to be executed to get block item instance.
     */
    public IntFunction<Block> currentFactory;

    public BlockItemFactoryEvent(BlockBase block, IntFunction<Block> currentFactory) {
        super(block);
        this.currentFactory = currentFactory;
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
