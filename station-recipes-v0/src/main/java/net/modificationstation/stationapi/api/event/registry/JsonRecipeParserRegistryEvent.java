package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.JsonRecipeParserRegistry;

/**
 * @deprecated Use {@link RecipeSerializerRegistryEvent} instead.
 */
@Deprecated
@EventPhases(StationAPI.INTERNAL_PHASE)
public class JsonRecipeParserRegistryEvent extends RegistryEvent<JsonRecipeParserRegistry> {
    public JsonRecipeParserRegistryEvent() {
        super(JsonRecipeParserRegistry.INSTANCE);
    }
}
