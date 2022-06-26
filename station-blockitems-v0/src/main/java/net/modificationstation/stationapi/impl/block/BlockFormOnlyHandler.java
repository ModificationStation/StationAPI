package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.block.BlockItemToggle;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Lazy;
import net.modificationstation.stationapi.api.util.UnsafeProvider;

import java.util.function.Consumer;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class BlockFormOnlyHandler {

    public static final Lazy<Block> EMPTY_BLOCK_ITEM = new Lazy<>(() -> {
        try {
            return (Block) UnsafeProvider.theUnsafe.allocateInstance(Block.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    });

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 - Integer.MAX_VALUE / 8)
    private static void registerBlocksAsBlockOnly(BlockRegistryEvent event) {
        Consumer<BlockBase> c = block -> ((BlockItemToggle<?>) block).disableBlockItem();
        c.accept(BlockBase.BY_ID[0]);
//        c.accept(BlockBase.FLOWING_WATER);
//        c.accept(BlockBase.STILL_WATER);
//        c.accept(BlockBase.FLOWING_LAVA);
//        c.accept(BlockBase.STILL_LAVA);
        c.accept(BlockBase.BED);
//        c.accept(BlockBase.PISTON_HEAD);
//        c.accept(BlockBase.MOVING_PISTON);
//        c.accept(BlockBase.DOUBLE_STONE_SLAB);
//        c.accept(BlockBase.FIRE);
//        c.accept(BlockBase.REDSTONE_DUST);
        c.accept(BlockBase.CROPS);
//        c.accept(BlockBase.FURNACE_LIT);
        c.accept(BlockBase.STANDING_SIGN);
        c.accept(BlockBase.WOOD_DOOR);
//        c.accept(BlockBase.WALL_SIGN);
        c.accept(BlockBase.IRON_DOOR);
//        c.accept(BlockBase.REDSTONE_ORE_LIT);
//        c.accept(BlockBase.REDSTONE_TORCH);
        c.accept(BlockBase.SUGAR_CANES);
//        c.accept(BlockBase.PORTAL);
        c.accept(BlockBase.CAKE);
        c.accept(BlockBase.REDSTONE_REPEATER);
//        c.accept(BlockBase.REDSTONE_REPEATER_LIT);
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerBlockItem(BlockItemFactoryEvent event) {
        if (((BlockItemToggle<?>) event.block).isBlockItemDisabled()) event.cancel();
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void unregisterEmptyBlockItems(AfterBlockAndItemRegisterEvent event) {
        for (int i = 0; i < BlockBase.BY_ID.length; i++)
            if (ItemBase.byId[i] == EMPTY_BLOCK_ITEM.get())
                ItemBase.byId[i] = null;
    }
}
