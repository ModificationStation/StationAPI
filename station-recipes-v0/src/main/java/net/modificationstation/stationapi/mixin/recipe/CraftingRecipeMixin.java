package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CraftingRecipe.class)
public interface CraftingRecipeMixin extends StationRecipe {
    @Override
    default ItemStack[] getIngredients() {
        throw new UnsupportedOperationException("Your custom recipe registry needs to implement the methods found in \"net.modificationstation.stationapi.api.recipe.StationRecipe\"!");
    }

    @Override
    default ItemStack[] getOutputs() {
        throw new UnsupportedOperationException("Your custom recipe registry needs to implement the methods found in \"net.modificationstation.stationapi.api.recipe.StationRecipe\"!");
    }
}
