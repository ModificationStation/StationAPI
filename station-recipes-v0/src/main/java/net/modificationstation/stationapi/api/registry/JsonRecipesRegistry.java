package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.impl.recipe.RecipeManager;

import java.net.URL;
import java.util.Collections;
import java.util.Set;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

/**
 * @deprecated Use {@link RecipeManager} instead.
 */
@Deprecated
public final class JsonRecipesRegistry extends SimpleRegistry<Set<URL>> {

    @SuppressWarnings("CollectionContainsUrl")
    private static final Set<URL> EMPTY = Collections.emptySet();
    public static final RegistryKey<JsonRecipesRegistry> KEY = RegistryKey.ofRegistry(MODID.id("json_recipes"));
    public static final JsonRecipesRegistry INSTANCE = Registries.create(KEY, new JsonRecipesRegistry(), registry -> EMPTY, Lifecycle.experimental());

    private JsonRecipesRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
