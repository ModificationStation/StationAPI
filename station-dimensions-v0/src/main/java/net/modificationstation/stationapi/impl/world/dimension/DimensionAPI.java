package net.modificationstation.stationapi.impl.world.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.resource.language.LanguageManager;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class DimensionAPI {

    @Entrypoint.Namespace
    private static final Namespace NAMESPACE = Null.get();

    @EventListener
    private static void init(InitEvent event) {
        LOGGER.info("Adding Dimension API lang folder...");
        LanguageManager.addPath("/assets/" + NAMESPACE + "/lang", StationAPI.NAMESPACE);
    }
}
