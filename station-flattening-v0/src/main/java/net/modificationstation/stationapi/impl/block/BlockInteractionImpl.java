package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BeforeBlockRemoved;
import net.modificationstation.stationapi.api.event.block.BlockEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class BlockInteractionImpl {

    @EventListener
    private static void beforeBlockRemoved(BlockEvent.BeforeRemoved event) {
        if (event.block instanceof BeforeBlockRemoved listener) listener.beforeBlockRemoved(event.level, event.x, event.y, event.z);
    }
}
