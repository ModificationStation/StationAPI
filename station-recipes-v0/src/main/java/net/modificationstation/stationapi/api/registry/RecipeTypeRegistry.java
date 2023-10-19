package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.recipe.RecipeType;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class RecipeTypeRegistry extends SimpleRegistry<RecipeType<?>> {
    public static final RegistryKey<RecipeTypeRegistry> KEY = RegistryKey.ofRegistry(MODID.id("recipe_types"));
    public static final RecipeTypeRegistry INSTANCE = Registries.create(KEY, new RecipeTypeRegistry(), registry -> RecipeType.CRAFTING, Lifecycle.experimental());

    private RecipeTypeRegistry() {
        super(KEY, Lifecycle.experimental(), false);
    }
}
