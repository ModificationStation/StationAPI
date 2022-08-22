package net.modificationstation.stationapi.mixin.vanillafix.item.dye;

import net.minecraft.recipe.DyeRecipes;
import net.minecraft.recipe.RecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeRegistry.class)
public class MixinRecipeRegistry {

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/DyeRecipes;register(Lnet/minecraft/recipe/RecipeRegistry;)V"
            )
    )
    private void stopDyeRecipes(DyeRecipes instance, RecipeRegistry recipeRegistry) {}
}
