package net.modificationstation.stationapi.api.common.recipe;

import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.event.recipe.RecipeRegisterEvent;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.Registry;

import java.net.*;
import java.util.function.*;
/**
 * The JSON recipe parser registry that holds all JSON recipe parsers to automatically run when {@link RecipeRegisterEvent} event is called with a proper identifier.
 * @author mine_diver
 */
public final class JsonRecipeParserRegistry extends Registry<Consumer<URL>> {

    private JsonRecipeParserRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }

    public static final JsonRecipeParserRegistry INSTANCE = new JsonRecipeParserRegistry(Identifier.of(StationAPI.MODID, "json_recipe_parsers"));
}
