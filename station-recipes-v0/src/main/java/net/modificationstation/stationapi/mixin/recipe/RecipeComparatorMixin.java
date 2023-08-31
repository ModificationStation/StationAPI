package net.modificationstation.stationapi.mixin.recipe;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.modificationstation.stationapi.impl.recipe.StationShapedRecipe;
import net.modificationstation.stationapi.impl.recipe.StationShapelessRecipe;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.recipe.RecipeRegistry$1")
public class RecipeComparatorMixin {
    @WrapOperation(
            method = "method_543",
            at = @At(
                    value = "CONSTANT",
                    opcode = Opcodes.INSTANCEOF,
                    args = "classValue=net/minecraft/recipe/ShapelessRecipe"
            )
    )
    private boolean stationapi_accountForStationShapelessRecipe(Object recipe, Operation<Boolean> otherConditions) {
        return recipe instanceof StationShapelessRecipe || otherConditions.call(recipe);
    }

    @WrapOperation(
            method = "method_543",
            at = @At(
                    value = "CONSTANT",
                    opcode = Opcodes.INSTANCEOF,
                    args = "classValue=net/minecraft/recipe/ShapedRecipe"
            )
    )
    private boolean stationapi_accountForStationShapedRecipe(Object recipe, Operation<Boolean> otherConditions) {
        return recipe instanceof StationShapedRecipe || otherConditions.call(recipe);
    }
}
