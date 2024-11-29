package net.modificationstation.stationapi.impl.recipe;

import com.mojang.datafixers.util.Either;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;

import java.util.*;
import java.util.function.Function;

public class StationShapelessRecipe implements CraftingRecipe, StationRecipe {

    private static final Random RANDOM = new Random();

    private final Either<TagKey<Item>, ItemStack>[] ingredients;
    public final ItemStack output;
    private final BitSet matchedIngredients;

    public StationShapelessRecipe(ItemStack output, Either<TagKey<Item>, ItemStack>[] ingredients) {
        this.ingredients = ingredients;
        this.output = output;
        matchedIngredients = new BitSet(ingredients.length);
    }

    @Override
    public boolean matches(CraftingInventory arg) {
        matchedIngredients.clear();
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                ItemStack itemToTest = arg.getStack(x, y);
                if (itemToTest == null) continue;
                boolean noMatch = true;
                for (int i = 0, ingredientsLength = ingredients.length; i < ingredientsLength; i++) {
                    if (matchedIngredients.get(i)) continue;
                    Either<TagKey<Item>, ItemStack> ingredient = ingredients[i];
                    Optional<TagKey<Item>> tagOpt = ingredient.left();
                    boolean equals = false;
                    if (tagOpt.isPresent()) {
                        equals = itemToTest.isIn(tagOpt.get());
                    } else {
                        Optional<ItemStack> itemOpt = ingredient.right();
                        if (itemOpt.isPresent()) {
                            ItemStack item = itemOpt.get();
                            boolean ignoreDamage = item.getDamage() == -1;
                            if (ignoreDamage) item.setDamage(itemToTest.getDamage());
                            equals = item.isItemEqual(itemToTest);
                            if (ignoreDamage) item.setDamage(-1);
                        }
                    }
                    if (equals) {
                        matchedIngredients.set(i);
                        noMatch = false;
                        break;
                    }
                }
                if (noMatch) return false;
            }
        }
        return matchedIngredients.nextClearBit(0) >= ingredients.length;
    }

    @Override
    public ItemStack craft(CraftingInventory arg) {
        return output.copy();
    }

    @Override
    public int getSize() {
        return ingredients.length;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public ItemStack[] getIngredients() {
        ItemStack[] inputs = new ItemStack[ingredients.length];
        for (int i = 0, ingredientsLength = ingredients.length; i < ingredientsLength; i++)
            inputs[i] = ingredients[i].map(tag -> new ItemStack(ItemRegistry.INSTANCE.getEntryList(tag).orElseThrow(() -> new RuntimeException("Identifier ingredient \"" + tag.id() + "\" has no entry in the tag registry!")).getRandom(RANDOM).orElseThrow().value()), Function.identity());
        return inputs;
    }

    @Override
    public ItemStack[] getOutputs() {
        return new ItemStack[] { output };
    }

    /**
     * To be renamed to getIngredients in a3, use with caution.
     */
    @Deprecated
    public Either<TagKey<Item>, ItemStack>[] getInputs() {
        //noinspection unchecked
        return (Either<TagKey<Item>, ItemStack>[]) Arrays.stream(ingredients).map(entry -> entry == null ? null : entry.mapRight(ItemStack::copy)).toArray(Either[]::new);
    }
}
