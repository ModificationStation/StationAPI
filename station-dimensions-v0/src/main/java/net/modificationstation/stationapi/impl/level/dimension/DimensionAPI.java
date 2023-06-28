package net.modificationstation.stationapi.impl.level.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.lang.I18n;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class DimensionAPI {

    @Entrypoint.ModID
    private static final ModID MODID = Null.get();

    @EventListener
    private static void init(InitEvent event) {
        LOGGER.info("Adding Dimension API lang folder...");
        I18n.addLangFolder(StationAPI.MODID, "/assets/" + MODID + "/lang");
    }
}
