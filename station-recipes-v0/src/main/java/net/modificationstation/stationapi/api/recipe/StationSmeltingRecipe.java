package net.modificationstation.stationapi.api.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import lombok.val;
import net.minecraft.item.ItemInstance;
import net.minecraft.tileentity.TileEntityFurnace;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.JsonHelper;

import java.util.Random;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class StationSmeltingRecipe implements StationRecipe<TileEntityFurnace> {
    private static final Random RANDOM = new Random();

    private final Ingredient input;
    private final ItemInstance output;
    private final boolean dataDriven;

    public StationSmeltingRecipe(Ingredient input, ItemInstance output) {
        this(input, output, false);
    }

    private StationSmeltingRecipe(Ingredient input, ItemInstance output, boolean dataDriven) {
        this.input = input;
        this.output = output;
        this.dataDriven = dataDriven;
    }

    @Override
    public boolean stationapi_matches(TileEntityFurnace inventory) {
        return input.test(inventory.getInventoryItem(0));
    }

    @Override
    public ItemInstance stationapi_craft(TileEntityFurnace inventory) {
        return output.copy();
    }

    @Override
    public ItemInstance[] getIngredients() {
        return new ItemInstance[] { input.getRandom(RANDOM) };
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[] { output };
    }

    @Override
    public boolean isDataDriven() {
        return dataDriven;
    }

    @Override
    public RecipeType<StationSmeltingRecipe> getType() {
        return RecipeType.SMELTING;
    }

    public static class Serializer implements RecipeSerializer<StationSmeltingRecipe> {
        @Override
        public StationSmeltingRecipe read(Identifier identifier, JsonObject jsonObject) {
            val jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
            val ingredient = Ingredient.fromJson(jsonElement, false);
            ItemInstance itemStack;
            if (JsonHelper.hasString(jsonObject, "result")) {
                String string2 = JsonHelper.getString(jsonObject, "result");
                val identifier2 = Identifier.of(string2);
                itemStack = new ItemInstance(ItemRegistry.INSTANCE.getOrEmpty(identifier2).orElseThrow(() -> new IllegalStateException("Item: " + string2 + " does not exist")));
            } else itemStack = StationShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new StationSmeltingRecipe(ingredient, itemStack, true);
        }
    }
}
