package net.modificationstation.stationapi.impl;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.level.BlockRemovedEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class BlockRemovedImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void onBlockRemoved(BlockRemovedEvent event) {

    }
}
