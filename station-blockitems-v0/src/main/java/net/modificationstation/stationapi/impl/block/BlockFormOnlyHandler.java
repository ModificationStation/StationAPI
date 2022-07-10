package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.block.BlockItemToggle;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Lazy;
import net.modificationstation.stationapi.api.util.UnsafeProvider;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class BlockFormOnlyHandler {

    public static final Lazy<Block> EMPTY_BLOCK_ITEM = new Lazy<>(() -> {
        try {
            return (Block) UnsafeProvider.theUnsafe.allocateInstance(Block.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    });

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerBlockItem(BlockItemFactoryEvent event) {
        if (((BlockItemToggle<?>) event.block).isBlockItemDisabled()) event.cancel();
    }

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void unregisterEmptyBlockItems(AfterBlockAndItemRegisterEvent event) {
        for (int i = 0; i < BlockBase.BY_ID.length; i++)
            if (ItemBase.byId[i] == EMPTY_BLOCK_ITEM.get())
                ItemBase.byId[i] = null;
    }
}
