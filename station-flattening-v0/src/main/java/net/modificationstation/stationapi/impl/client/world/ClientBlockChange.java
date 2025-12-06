package net.modificationstation.stationapi.impl.client.world;

import net.minecraft.block.Block;
import net.minecraft.world.ClientWorld;
import net.modificationstation.stationapi.api.block.BlockState;

public class ClientBlockChange extends ClientWorld.BlockReset {
    public int stateId;

    public ClientBlockChange(ClientWorld world, int x, int y, int z, BlockState state, int metadata) {
        world.super(x, y, z, state.getBlock().id, metadata);
        stateId = Block.STATE_IDS.getRawId(state);
    }
}
