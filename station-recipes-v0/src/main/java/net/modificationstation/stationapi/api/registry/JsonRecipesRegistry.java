package net.modificationstation.stationapi.api.registry;

import net.modificationstation.stationapi.api.StationAPI;

import java.net.URL;
import java.util.Set;

public final class JsonRecipesRegistry extends Registry<Set<URL>> {

    private JsonRecipesRegistry(Identifier identifier) {
        super(identifier);
    }

    public static final JsonRecipesRegistry INSTANCE = new JsonRecipesRegistry(Identifier.of(StationAPI.MODID, "json_recipes"));
}
