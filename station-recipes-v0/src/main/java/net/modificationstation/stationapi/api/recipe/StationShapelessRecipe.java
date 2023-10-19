package net.modificationstation.stationapi.api.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.val;
import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.JsonHelper;

import java.util.BitSet;
import java.util.Random;

public class StationShapelessRecipe implements Recipe {
    private static final Random RANDOM = new Random();

    private final Ingredient[] ingredients;
    private final ItemInstance output;
    private final BitSet matchedIngredients;
    private final boolean dataDriven;

    public StationShapelessRecipe(ItemInstance output, Ingredient[] ingredients) {
        this(output, ingredients, false);
    }

    private StationShapelessRecipe(ItemInstance output, Ingredient[] ingredients, boolean dataDriven) {
        this.ingredients = ingredients;
        this.output = output;
        matchedIngredients = new BitSet(ingredients.length);
        this.dataDriven = dataDriven;
    }

    @Override
    public boolean canCraft(Crafting arg) {
        matchedIngredients.clear();
        for (int y = 0; y < 3; ++y)
            for (int x = 0; x < 3; ++x) {
                ItemInstance itemToTest = arg.getInventoryItemXY(x, y);
                if (itemToTest == null) continue;
                boolean noMatch = true;
                for (int i = 0, ingredientsLength = ingredients.length; i < ingredientsLength; i++) {
                    if (matchedIngredients.get(i)) continue;
                    if (ingredients[i].test(itemToTest)) {
                        matchedIngredients.set(i);
                        noMatch = false;
                        break;
                    }
                }
                if (noMatch) return false;
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
            inputs[i] = ingredients[i].getRandom(RANDOM);
        return inputs;
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[] { output };
    }

    @Override
    public boolean isDataDriven() {
        return dataDriven;
    }

    public static class Serializer implements RecipeSerializer<StationShapelessRecipe> {
        @Override
        public StationShapelessRecipe read(Identifier identifier, JsonObject jsonObject) {
            val ingredients = Serializer.getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
            if (ingredients.length == 0) throw new JsonParseException("No ingredients for shapeless recipe");
            if (ingredients.length > 9) throw new JsonParseException("Too many ingredients for shapeless recipe");
            val output = StationShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new StationShapelessRecipe(output, ingredients, true);
        }

        private static Ingredient[] getIngredients(JsonArray json) {
            val ingredients = new Ingredient[json.size()];
            for (int i = 0; i < json.size(); ++i) {
                val ingredient = Ingredient.fromJson(json.get(i), false);
                if (ingredient.isEmpty()) continue;
                ingredients[i] = ingredient;
            }
            return ingredients;
        }
    }
}
