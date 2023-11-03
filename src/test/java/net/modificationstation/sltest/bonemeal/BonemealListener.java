package net.modificationstation.sltest.bonemeal;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.bonemeal.BonemealAPI;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;

public class BonemealListener {
    @EventListener(priority = ListenerPriority.LOW)
    public void registerItems(BlockRegistryEvent event) {
        BonemealAPI.addPlant(Block.SAND.getDefaultState(), Block.BOOKSHELF.getDefaultState(), 1);
    }
}
