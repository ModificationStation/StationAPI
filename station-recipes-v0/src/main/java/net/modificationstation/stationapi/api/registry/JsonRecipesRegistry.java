package net.modificationstation.stationapi.api.registry;

import net.modificationstation.stationapi.api.StationAPI;

import java.net.*;
import java.util.*;

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
