package net.modificationstation.stationapi.impl.common.mod;

import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.event.mod.InitEvent;
import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;

public class InitImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    public static void onInitialization(InitEvent event) {
        StationAPI.EVENT_BUS.post(new RegistryEvent.JsonRecipeParsers());
    }
}
