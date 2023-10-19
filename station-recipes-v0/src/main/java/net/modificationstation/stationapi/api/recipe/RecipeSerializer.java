package net.modificationstation.stationapi.api.recipe;

import com.google.gson.JsonObject;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.RecipeSerializerRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import static net.modificationstation.stationapi.api.registry.ModID.MINECRAFT;

/**
 * The recipe serializer controls the serialization and deserialization of
 * recipe content. The caller should handle the serialization of recipes' IDs.
 * 
 * <p>Even though they are referred to by the {@code type} field in recipe
 * JSON format, they are stored in a registry with key
 * {@code minecraft:root/minecraft:recipe_serializer}, and is hence named.
 * 
 * <p>If a recipe's serializer exists only on the server but not on the
 * client, the client will disconnect upon receiving the recipe; if a
 * recipe JSON intended for an absent recipe serializer is read, it is
 * skipped.
 */
public interface RecipeSerializer<T extends StationRecipe<?>> {
    RecipeSerializer<StationShapedRecipe> SHAPED = RecipeSerializer.register(MINECRAFT.id("crafting_shaped"), new StationShapedRecipe.Serializer());
    RecipeSerializer<StationShapelessRecipe> SHAPELESS = RecipeSerializer.register(MINECRAFT.id("crafting_shapeless"), new StationShapelessRecipe.Serializer());
    RecipeSerializer<StationSmeltingRecipe> SMELTING = RecipeSerializer.register(MINECRAFT.id("smelting"), new StationSmeltingRecipe.Serializer());

    /**
     * Reads a recipe from a JSON object.
     * 
     * @implNote If this throws any exception besides {@link com.google.gson.JsonParseException}
     * and {@link IllegalArgumentException}, it will terminate and affect loading
     * of all recipes from data packs beyond the current recipe.
     * 
     * @throws com.google.gson.JsonParseException if the recipe JSON is incorrect
     * @return the read recipe
     * 
     * @param json the recipe JSON
     * @param id the recipe's ID
     */
    T read(Identifier id, JsonObject json);

    static <S extends RecipeSerializer<T>, T extends StationRecipe<?>> S register(Identifier id, S serializer) {
        return Registry.register(RecipeSerializerRegistry.INSTANCE, id, serializer);
    }
}

