package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.ShapedRecipe;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShapedRecipe.class)
public class MixinShapedRecipe implements StationRecipe {

    @Shadow private int width;
    @Shadow private int height;
    @Shadow private ItemStack output;

    @Shadow private ItemStack[] ingredients;

    @Override
    public ItemStack[] getIngredients() {
        ItemStack[] itemInstances = new ItemStack[9];
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int localId = (h * width) + w;
                int id = (h * 3) + w;
                itemInstances[id] = ingredients[localId];
            }
        }
        return itemInstances;
    }

    @Override
    public ItemStack[] getOutputs() {
        return new ItemStack[] { output };
    }
}
