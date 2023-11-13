package net.modificationstation.stationapi.api.recipe;

import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.chars.Char2ReferenceMap;
import it.unimi.dsi.fastutil.chars.Char2ReferenceOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipeManager;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.API;
import net.modificationstation.stationapi.impl.recipe.StationShapedRecipe;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;

import java.util.Optional;

public class CraftingRegistry {

    @API
    public static void addShapedRecipe(ItemStack output, Object... recipe) {
        StringBuilder ingredients = new StringBuilder();
        int currentElement = 0;
        int width = 0;
        int height = 0;
        if (recipe[currentElement] instanceof String[] grid) {
            currentElement++;
            for (int i = 0; i < grid.length; ++i) {
                String ingredientsLine = grid[i];
                ++height;
                width = ingredientsLine.length();
                ingredients.append(ingredientsLine);
            }
        } else while (recipe[currentElement] instanceof String ingredientsLine) {
            currentElement++;
            ++height;
            width = ingredientsLine.length();
            ingredients.append(ingredientsLine);
        }
        Char2ReferenceMap<Either<TagKey<Item>, ItemStack>> keyToIngredient = new Char2ReferenceOpenHashMap<>();
        for (; currentElement < recipe.length; currentElement += 2) {
            char c = (char) recipe[currentElement];
            Either<TagKey<Item>, ItemStack> ingredient = null;
            Object element = recipe[currentElement + 1];
            if (element instanceof TagKey<?> tag) {
                Optional<TagKey<Item>> itemTagOpt = tag.tryCast(ItemRegistry.KEY);
                if (itemTagOpt.isPresent()) ingredient = Either.left(itemTagOpt.get());
            } else if (element instanceof Block block) ingredient = Either.right(new ItemStack(block, 1, -1));
            else if (element instanceof ItemStack stack) ingredient = Either.right(stack);
            else if (element instanceof Item item) ingredient = Either.right(new ItemStack(item));
            keyToIngredient.put(c, ingredient);
        }
        //noinspection unchecked
        Either<TagKey<Item>, ItemStack>[] grid = new Either[width * height];
        for (int i = 0; i < width * height; ++i) {
            char c = ingredients.charAt(i);
            grid[i] = keyToIngredient.containsKey(c) ? keyToIngredient.get(c).mapRight(ItemStack::copy) : null;
        }
        //noinspection unchecked
        CraftingRecipeManager.getInstance().getRecipes().add(new StationShapedRecipe(width, height, grid, output));
    }

    @API
    public static void addShapelessRecipe(ItemStack output, Object... ingredients) {
        //noinspection unchecked
        Either<TagKey<Item>, ItemStack>[] checkedIngredients = new Either[ingredients.length];
        for (int i = 0, ingredientsLength = ingredients.length; i < ingredientsLength; i++) {
            Object ingredient = ingredients[i];
            if (ingredient instanceof ItemStack stack) checkedIngredients[i] = Either.right(stack.copy());
            else if (ingredient instanceof Item item) checkedIngredients[i] = Either.right(new ItemStack(item));
            else if (ingredient instanceof Block block) checkedIngredients[i] = Either.right(new ItemStack(block));
            else if (ingredient instanceof TagKey<?> tag) {
                Optional<TagKey<Item>> itemTagOpt = tag.tryCast(ItemRegistry.KEY);
                if (itemTagOpt.isPresent())
                    checkedIngredients[i] = Either.left(itemTagOpt.get());
            } else throw new RuntimeException("Invalid shapeless recipe ingredient of type " + ingredient.getClass().getName() + "!");
        }
        //noinspection unchecked
        CraftingRecipeManager.getInstance().getRecipes().add(new StationShapelessRecipe(output, checkedIngredients));
    }
}
