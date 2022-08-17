package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.event.recipe.RecipeRegisterEvent;

import java.net.URL;
import java.util.function.Consumer;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

/**
 * The JSON recipe parser registry that holds all JSON recipe parsers to automatically run when {@link RecipeRegisterEvent} event is called with a proper identifier.
 * @author mine_diver
 */
public final class JsonRecipeParserRegistry extends SimpleRegistry<Consumer<URL>> {

    public static final RegistryKey<Registry<Consumer<URL>>> KEY = RegistryKey.ofRegistry(MODID.id("json_recipe_parsers"));
    public static final JsonRecipeParserRegistry INSTANCE = Registry.create(KEY, new JsonRecipeParserRegistry(), Lifecycle.experimental());

    private JsonRecipeParserRegistry() {
        super(KEY, Lifecycle.experimental(), null);
    }
}
