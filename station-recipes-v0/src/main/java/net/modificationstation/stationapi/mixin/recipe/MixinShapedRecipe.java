package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ShapedRecipe.class)
abstract class MixinShapedRecipe implements Recipe {

    @Shadow private int width;
    @Shadow private int height;
    @Shadow private ItemInstance output;

    @Shadow private ItemInstance[] ingredients;

    @Override
    public ItemInstance[] getIngredients() {
        ItemInstance[] itemInstances = new ItemInstance[9];
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
    public ItemInstance[] getOutputs() {
        return new ItemInstance[] { output };
    }
}
