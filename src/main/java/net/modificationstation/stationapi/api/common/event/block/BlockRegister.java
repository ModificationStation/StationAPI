package net.modificationstation.stationapi.api.common.event.block;

import net.modificationstation.stationapi.api.common.block.BlockRegistry;
import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;

public class BlockRegister extends RegistryEvent<BlockRegistry> {

    public BlockRegister() {
        super(BlockRegistry.INSTANCE);
    }
}
