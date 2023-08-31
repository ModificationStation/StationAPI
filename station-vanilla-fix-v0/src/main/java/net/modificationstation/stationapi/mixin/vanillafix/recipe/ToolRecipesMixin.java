package net.modificationstation.stationapi.mixin.vanillafix.recipe;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.minecraft.recipe.ToolRecipes;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.impl.vanillafix.recipe.VanillaTagRecipeFixImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ToolRecipes.class)
public class ToolRecipesMixin {
    @WrapWithCondition(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V"
            )
    )
    private boolean stationapi_tagifyShapedRecipes(
            RecipeRegistry registry, ItemInstance result, Object[] ingredientMap
    ) {
        boolean tagified = VanillaTagRecipeFixImpl.tagifyIngredients(ingredientMap);
        if (tagified) CraftingRegistry.addShapedRecipe(result, ingredientMap);
        return !tagified;
    }
}
