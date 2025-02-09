package net.modificationstation.stationapi.impl.recipe.json;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.JsonRecipeParserRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;

import java.lang.invoke.MethodHandles;

@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class JsonRecipeParserRegisterImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void onInitialization(InitEvent event) {
        StationAPI.EVENT_BUS.post(new JsonRecipeParserRegistryEvent());
    }
}
