package net.modificationstation.stationapi.api.recipe;

import net.minecraft.item.ItemStack;

public interface StationRecipe {

    ItemStack[] getIngredients();

    ItemStack[] getOutputs();
}
