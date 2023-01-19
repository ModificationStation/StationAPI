package net.modificationstation.stationapi.impl.client.render;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.resource.ClientResourcesReloadEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class StationRendererImpl {

    @Entrypoint.Logger("StationRenderer|Impl")
    public static final Logger LOGGER = Null.get();

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void resourceReload(ClientResourcesReloadEvent event) {

    }
}
