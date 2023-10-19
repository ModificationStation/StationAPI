package net.modificationstation.stationapi.api.recipe;

import net.minecraft.inventory.InventoryBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.util.Util;

public interface StationRecipe<T extends InventoryBase> {
    default boolean stationapi_matches(T inventory) {
        return Util.assertImpl();
    }

    default ItemInstance stationapi_craft(T inventory) {
        return Util.assertImpl();
    }

    default ItemInstance[] getIngredients() {
        return Util.assertImpl();
    }

    default ItemInstance[] getOutputs() {
        return Util.assertImpl();
    }

    default boolean isDataDriven() {
        return false;
    }

    default RecipeType<?> getType() {
        return Util.assertImpl();
    }
}
