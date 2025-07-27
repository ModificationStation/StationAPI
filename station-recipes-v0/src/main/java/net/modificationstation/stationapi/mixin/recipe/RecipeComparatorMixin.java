package net.modificationstation.stationapi.mixin.recipe;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.modificationstation.stationapi.impl.recipe.StationShapedRecipe;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(targets = "net.minecraft.recipe.CraftingRecipeManager$1")
class RecipeComparatorMixin {
    @WrapOperation(
            method = "compare(Lnet/minecraft/recipe/CraftingRecipe;Lnet/minecraft/recipe/CraftingRecipe;)I",
            constant = @Constant(classValue = ShapelessRecipe.class)
    )
    private boolean stationapi_accountForStationShapelessRecipe(Object recipe, Operation<Boolean> otherConditions) {
        return recipe instanceof StationShapelessRecipe || otherConditions.call(recipe);
    }

    @WrapOperation(
            method = "compare(Lnet/minecraft/recipe/CraftingRecipe;Lnet/minecraft/recipe/CraftingRecipe;)I",
            constant = @Constant(classValue = ShapedRecipe.class)
    )
    private boolean stationapi_accountForStationShapedRecipe(Object recipe, Operation<Boolean> otherConditions) {
        return recipe instanceof StationShapedRecipe || otherConditions.call(recipe);
    }
}
