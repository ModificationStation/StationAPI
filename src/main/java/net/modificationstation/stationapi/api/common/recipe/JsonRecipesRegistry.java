package net.modificationstation.stationapi.api.common.recipe;

import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.Registry;

import java.net.URL;
import java.util.Set;

public final class JsonRecipesRegistry extends Registry<Set<URL>> {

    private JsonRecipesRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }

    public static final JsonRecipesRegistry INSTANCE = new JsonRecipesRegistry(Identifier.of(StationAPI.MODID, "json_recipes"));
}
