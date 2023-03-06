package net.modificationstation.stationapi.mixin.recipe;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationapi.api.recipe.StationRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ShapelessRecipe.class)
public class MixinShapelessRecipe implements StationRecipe {

    @Shadow @Final private List<ItemInstance> input;

    @Shadow @Final private ItemInstance output;

    @Override
    public ItemInstance[] getIngredients() {
        return input.toArray(ItemInstance[]::new);
    }

    @Override
    public ItemInstance[] getOutputs() {
        return new ItemInstance[] { output };
    }
}
