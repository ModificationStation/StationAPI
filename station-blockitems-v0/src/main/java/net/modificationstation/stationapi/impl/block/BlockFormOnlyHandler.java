package net.modificationstation.stationapi.impl.block;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.block.BlockItemFactoryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.lang.invoke.MethodHandles;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class BlockFormOnlyHandler {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerBlockItem(BlockItemFactoryEvent event) {
        if (event.block.isAutoItemRegistrationDisabled()) event.cancel();
    }
}
