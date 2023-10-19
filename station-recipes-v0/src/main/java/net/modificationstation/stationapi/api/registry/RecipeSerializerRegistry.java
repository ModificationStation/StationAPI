package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.recipe.RecipeSerializer;
import net.modificationstation.stationapi.api.recipe.StationRecipe;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public final class RecipeSerializerRegistry extends SimpleRegistry<RecipeSerializer<? extends StationRecipe<?>>> {
    public static final RegistryKey<RecipeSerializerRegistry> KEY = RegistryKey.ofRegistry(MODID.id("recipe_serializers"));
    public static final RecipeSerializerRegistry INSTANCE = Registries.create(KEY, new RecipeSerializerRegistry(), registry -> RecipeSerializer.SHAPELESS, Lifecycle.experimental());

    private RecipeSerializerRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
