package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.inventory.Crafting;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.modificationstation.stationapi.api.recipe.RecipeType;
import net.modificationstation.stationapi.api.recipe.StationCraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Recipe.class)
interface MixinRecipe extends StationCraftingRecipe {
    @Shadow boolean canCraft(Crafting var1);

    @Shadow ItemInstance craft(Crafting var1);

    @Override
    default RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    default boolean stationapi_matches(Crafting inventory) {
        return canCraft(inventory);
    }

    @Override
    default ItemInstance stationapi_craft(Crafting inventory) {
        return craft(inventory);
    }
}
