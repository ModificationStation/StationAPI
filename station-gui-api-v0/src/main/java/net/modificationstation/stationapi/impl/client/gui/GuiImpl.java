package net.modificationstation.stationapi.impl.client.gui;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.language.LanguageManager;
import net.modificationstation.stationapi.api.util.Null;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class GuiImpl {

    @Entrypoint.ModID
    private static final ModID MODID = Null.get();

    @EventListener
    private static void registerLang(InitEvent event) {
        LOGGER.info("Adding GUI API lang folder...");
        LanguageManager.addPath("/assets/" + MODID + "/lang", StationAPI.MODID);
    }
}
