package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.BlockRegistry;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class BlockRegistryEvent extends RegistryEvent.EntryTypeBound<Block, BlockRegistry> {
    public BlockRegistryEvent() {
        super(BlockRegistry.INSTANCE);
    }
}
