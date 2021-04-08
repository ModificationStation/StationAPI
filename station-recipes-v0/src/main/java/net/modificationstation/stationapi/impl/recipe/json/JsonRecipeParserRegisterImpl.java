package net.modificationstation.stationapi.impl.recipe.json;

import net.modificationstation.stationapi.api.StationAPI;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.JsonRecipeParserRegistryEvent;
import net.modificationstation.stationapi.api.event.registry.RegistryEvent;

public class JsonRecipeParserRegisterImpl {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void onInitialization(InitEvent event) {
        StationAPI.EVENT_BUS.post(new JsonRecipeParserRegistryEvent());
    }
}
