package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Recipe.class)
public interface MixinRecipe extends StationRecipe {

    @Override
    default ItemInstance[] getIngredients() {
        throw new UnsupportedOperationException("Your custom recipe registry needs to implement the methods found in \"net.modificationstation.stationapi.api.recipe.StationRecipe\"!");
    }

    @Override
    default ItemInstance[] getOutputs() {
        throw new UnsupportedOperationException("Your custom recipe registry needs to implement the methods found in \"net.modificationstation.stationapi.api.recipe.StationRecipe\"!");
    }
}
