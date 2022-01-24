package net.modificationstation.stationapi.api.recipe;

import net.minecraft.item.ItemInstance;

public interface StationRecipe {

    ItemInstance[] getIngredients();

    ItemInstance[] getOutputs();
}
