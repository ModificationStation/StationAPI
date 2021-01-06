package net.modificationstation.stationapi.api.common.recipe;

import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.Registry;

import java.net.URL;
import java.util.function.Consumer;

public final class JsonRecipeParserRegistry extends Registry<Consumer<URL>> {

    private JsonRecipeParserRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }

    public static final JsonRecipeParserRegistry INSTANCE = new JsonRecipeParserRegistry(Identifier.of(StationAPI.INSTANCE.getModID(), "json_recipe_parsers"));
}
