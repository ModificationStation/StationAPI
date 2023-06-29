package net.modificationstation.stationapi.impl.client.level;

import net.minecraft.block.BlockBase;
import net.minecraft.client.level.ClientLevel;
import net.modificationstation.stationapi.api.block.BlockState;

public class ClientBlockChange extends ClientLevel.class_456 {
    public int stateId;

    public ClientBlockChange(ClientLevel world, int x, int y, int z, BlockState state, int metadata) {
        world.super(x, y, z, state.getBlock().id, metadata);
        stateId = BlockBase.STATE_IDS.getRawId(state);
    }
}
