package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ShapelessRecipe.class)
class ShapelessRecipeMixin implements StationRecipe {
    @Shadow @Final private List<ItemStack> input;

    @Shadow @Final private ItemStack output;

    @Override
    public ItemStack[] getIngredients() {
        return input.toArray(ItemStack[]::new);
    }

    @Override
    public ItemStack[] getOutputs() {
        return new ItemStack[] { output };
    }
}
