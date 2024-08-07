package net.modificationstation.stationapi.api.recipe;

import net.minecraft.item.ItemStack;

/**
 * This entire inferface is slated for removal in alpha 3.
 * Stop using it, it's shit and was made specifically for HMI, for all the wrong reasons.
 * - calm
 */
@Deprecated(forRemoval = true)
public interface StationRecipe {

    @Deprecated(forRemoval = true)
    ItemStack[] getIngredients();

    @Deprecated(forRemoval = true)
    ItemStack[] getOutputs();
}
