package net.modificationstation.stationapi.impl.client.world;

import net.minecraft.block.Block;
import net.minecraft.class_454;
import net.modificationstation.stationapi.api.block.BlockState;

public class ClientBlockChange extends class_454.class_456 {
    public int stateId;

    public ClientBlockChange(class_454 world, int x, int y, int z, BlockState state, int metadata) {
        world.super(x, y, z, state.getBlock().id, metadata);
        stateId = Block.STATE_IDS.getRawId(state);
    }
}
