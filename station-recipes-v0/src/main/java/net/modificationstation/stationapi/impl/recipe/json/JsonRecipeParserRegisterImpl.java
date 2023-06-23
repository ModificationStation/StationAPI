package net.modificationstation.stationapi.impl.recipe.json;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.JsonRecipeParserRegistryEvent;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class JsonRecipeParserRegisterImpl {

    @EventListener
    private static void onInitialization(InitEvent event) {
        StationAPI.EVENT_BUS.post(new JsonRecipeParserRegistryEvent());
    }
}
