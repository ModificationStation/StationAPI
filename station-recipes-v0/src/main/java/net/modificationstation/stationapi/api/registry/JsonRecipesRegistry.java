package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;

import java.net.URL;
import java.util.Set;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public final class JsonRecipesRegistry extends SimpleRegistry<Set<URL>> {

    public static final RegistryKey<JsonRecipesRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("json_recipes"));
    public static final JsonRecipesRegistry INSTANCE = Registries.create(KEY, new JsonRecipesRegistry(), Lifecycle.experimental());

    private JsonRecipesRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
