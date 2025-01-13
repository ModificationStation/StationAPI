package net.modificationstation.stationapi.impl.recipe.json;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.JsonRecipeParserRegistryEvent;

import java.lang.invoke.MethodHandles;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class JsonRecipeParserRegisterImpl {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void onInitialization(InitEvent event) {
        StationAPI.EVENT_BUS.post(new JsonRecipeParserRegistryEvent());
    }
}
