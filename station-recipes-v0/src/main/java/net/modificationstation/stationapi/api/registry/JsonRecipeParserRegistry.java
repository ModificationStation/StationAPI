package net.modificationstation.stationapi.api.registry;

import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;

import java.net.URL;
import java.util.function.Consumer;
/**
 * The JSON recipe parser registry that holds all JSON recipe parsers to automatically run when {@link RecipeRegisterEvent} event is called with a proper identifier.
 * @author mine_diver
 */
public final class JsonRecipeParserRegistry extends Registry<Consumer<URL>> {

    private JsonRecipeParserRegistry(Identifier identifier) {
        super(identifier);
    }

    public static final JsonRecipeParserRegistry INSTANCE = new JsonRecipeParserRegistry(Identifier.of(StationAPI.MODID, "json_recipe_parsers"));
}
