package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.BlockBase;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Lazy;
import net.modificationstation.stationapi.api.util.UnsafeProvider;
import net.modificationstation.stationapi.api.util.Util;

import java.util.*;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class BlockFormOnlyHandler {

    public static final Lazy<Block> EMPTY_BLOCK_ITEM = new Lazy<>(() -> {
        try {
            return (Block) UnsafeProvider.theUnsafe.allocateInstance(Block.class);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    });
    private static final Lazy<Set<BlockBase>> BLOCK_ONLY_BLOCKS = new Lazy<>(() -> Util.make(Collections.newSetFromMap(new IdentityHashMap<>()), m -> {
//        m.add(BlockBase.FLOWING_WATER);
//        m.add(BlockBase.STILL_WATER);
//        m.add(BlockBase.FLOWING_LAVA);
//        m.add(BlockBase.STILL_LAVA);
        m.add(BlockBase.BED);
//        m.add(BlockBase.PISTON_HEAD);
//        m.add(BlockBase.MOVING_PISTON);
//        m.add(BlockBase.DOUBLE_STONE_SLAB);
//        m.add(BlockBase.FIRE);
//        m.add(BlockBase.REDSTONE_DUST);
        m.add(BlockBase.CROPS);
//        m.add(BlockBase.FURNACE_LIT);
        m.add(BlockBase.STANDING_SIGN);
        m.add(BlockBase.WOOD_DOOR);
//        m.add(BlockBase.WALL_SIGN);
        m.add(BlockBase.IRON_DOOR);
//        m.add(BlockBase.REDSTONE_ORE_LIT);
//        m.add(BlockBase.REDSTONE_TORCH);
        m.add(BlockBase.SUGAR_CANES);
//        m.add(BlockBase.PORTAL);
        m.add(BlockBase.CAKE);
        m.add(BlockBase.REDSTONE_REPEATER);
//        m.add(BlockBase.REDSTONE_REPEATER_LIT);
    }));

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerBlockItem(BlockItemFactoryEvent event) {
        if (BLOCK_ONLY_BLOCKS.get().contains(event.block))
            event.cancel();
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void unregisterEmptyBlockItems(AfterBlockAndItemRegisterEvent event) {
        for (int i = 0; i < BlockBase.BY_ID.length; i++)
            if (ItemBase.byId[i] == EMPTY_BLOCK_ITEM.get())
                ItemBase.byId[i] = null;
    }
}
