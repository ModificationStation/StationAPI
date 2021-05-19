package net.modificationstation.stationapi.impl.lang;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.PreInitEvent;
import net.modificationstation.stationapi.api.lang.I18n;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.ModID;

import java.net.*;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class LangLoader {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void loadLang(PreInitEvent event) {
        LOGGER.info("Searching for lang paths...");
        FabricLoader.getInstance().getAllMods().forEach(modContainer -> {
            ModID modID = ModID.of(modContainer);
            String pathName = "/assets/" + modID + "/" + MODID + "/lang";
            URL path = LangLoader.class.getResource(pathName);
            if (path != null) {
                I18n.addLangFolder(modID, pathName);
                LOGGER.info(String.format("Registered lang path for %s.", modID));
            }
        });
    }
}
