package net.modificationstation.stationapi.api.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeRegistry;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.RecipeTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.modificationstation.stationapi.api.registry.ModID.MINECRAFT;

/**
 * The recipe type allows matching recipes more efficiently by only checking
 * recipes under a given type.
 * 
 * @param <T> the common supertype of recipes within a recipe type
 */
public interface RecipeType<T extends StationRecipe<?>> {
    @SuppressWarnings("unchecked")
    RecipeType<Recipe> CRAFTING = RecipeType.<Recipe>register(
            MINECRAFT.id("crafting"),
            RecipeRegistry.getInstance().getRecipes()::removeIf,
            RecipeRegistry.getInstance().getRecipes()::add
    );
    @SuppressWarnings("unchecked")
    RecipeType<StationSmeltingRecipe> SMELTING = RecipeType.register(
            MINECRAFT.id("smelting"),
            filter -> SmeltingRecipeRegistry.getInstance().getRecipes().keySet().removeIf(o -> o instanceof StationSmeltingRecipe recipe && filter.test(recipe)),
            recipe -> SmeltingRecipeRegistry.getInstance().getRecipes().put(recipe, null)
    );

    static <T extends StationRecipe<?>> RecipeType<T> register(final Identifier id) {
        return Registry.register(RecipeTypeRegistry.INSTANCE, id, new RecipeType<T>() {
            @Override
            public String toString() {
                return id.toString();
            }
        });
    }

    static <T extends StationRecipe<?>> RecipeType<T> register(
            final Identifier id,
            Consumer<Predicate<T>> recipesRemover,
            Consumer<T> recipeAdder
    ) {
        return Registry.register(RecipeTypeRegistry.INSTANCE, id, new RecipeType<T>() {
            @Override
            public void removeRecipes(Predicate<T> filter) {
                recipesRemover.accept(filter);
            }

            @Override
            public void addRecipe(T recipe) {
                recipeAdder.accept(recipe);
            }

            @Override
            public String toString() {
                return id.toString();
            }
        });
    }

    default void removeRecipes(Predicate<T> filter) {}

    default void addRecipe(T recipe) {}
}

