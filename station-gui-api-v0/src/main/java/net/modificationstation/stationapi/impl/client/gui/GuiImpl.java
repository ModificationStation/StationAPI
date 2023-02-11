package net.modificationstation.stationapi.impl.client.gui;

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
public final class GuiImpl {

    @Entrypoint.ModID
    private static final ModID MODID = Null.get();

    @EventListener(numPriority = Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4)
    private static void registerLang(InitEvent event) {
        LOGGER.info("Adding GUI API lang folder...");
        I18n.addLangFolder(StationAPI.MODID, "/assets/" + MODID + "/lang");
    }
}
