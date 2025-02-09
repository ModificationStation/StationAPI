package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.JsonRecipeParserRegistry;

import java.net.URL;
import java.util.function.Consumer;

@EventPhases(StationAPI.INTERNAL_PHASE)
public class JsonRecipeParserRegistryEvent extends RegistryEvent.EntryTypeBound<Consumer<URL>, JsonRecipeParserRegistry> {
    public JsonRecipeParserRegistryEvent() {
        super(JsonRecipeParserRegistry.INSTANCE);
    }
}
