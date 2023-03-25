package net.modificationstation.stationapi.impl.recipe;

import com.mojang.datafixers.util.Either;
import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.item.StationItemStack;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;

import java.util.BitSet;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public class StationShapelessRecipe implements Recipe, StationRecipe {

    private static final Random RANDOM = new Random();

    private final Either<TagKey<ItemBase>, ItemInstance>[] ingredients;
    private final ItemInstance output;
    private final BitSet matchedIngredients;

    public StationShapelessRecipe(ItemInstance output, Either<TagKey<ItemBase>, ItemInstance>[] ingredients) {
        this.ingredients = ingredients;
        this.output = output;
        matchedIngredients = new BitSet(ingredients.length);
    }

    @Override
    public boolean canCraft(Crafting arg) {
        matchedIngredients.clear();
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                ItemInstance itemToTest = arg.getInventoryItemXY(x, y);
                if (itemToTest == null) continue;
                boolean noMatch = true;
                for (int i = 0, ingredientsLength = ingredients.length; i < ingredientsLength; i++) {
                    if (matchedIngredients.get(i)) continue;
                    Either<TagKey<ItemBase>, ItemInstance> ingredient = ingredients[i];
                    Optional<TagKey<ItemBase>> tagOpt = ingredient.left();
                    boolean equals = false;
                    if (tagOpt.isPresent()) {
                        equals = StationItemStack.class.cast(itemToTest).isIn(tagOpt.get());
                    } else {
                        Optional<ItemInstance> itemOpt = ingredient.right();
                        if (itemOpt.isPresent()) {
                            ItemInstance item = itemOpt.get();
                            boolean ignoreDamage = item.getDamage() == -1;
                            if (ignoreDamage) item.setDamage(itemToTest.getDamage());
                            equals = item.isDamageAndIDIdentical(itemToTest);
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
    public ItemInstance craft(Crafting arg) {
        return output.copy();
    }

    @Override
    public int getIngredientCount() {
        return ingredients.length;
    }

    @Override
    public ItemInstance getOutput() {
        return output;
    }

    @Override
    public ItemInstance[] getIngredients() {
        ItemInstance[] inputs = new ItemInstance[ingredients.length];
        for (int i = 0, ingredientsLength = ingredients.length; i < ingredientsLength; i++)
            inputs[i] = ingredients[i].map(tag -> new ItemInstance(ItemRegistry.INSTANCE.getEntryList(tag).orElseThrow(() -> new RuntimeException("Identifier ingredient \"" + tag.id() + "\" has no entry in the tag registry!")).getRandom(RANDOM).orElseThrow().value()), Function.identity());
        return inputs;
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[] { output };
    }
}
