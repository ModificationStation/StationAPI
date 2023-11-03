package net.modificationstation.stationapi.mixin.vanillafix.recipe;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipeManager;
import net.minecraft.recipe.WeaponRecipes;
import net.modificationstation.stationapi.api.recipe.CraftingRegistry;
import net.modificationstation.stationapi.impl.vanillafix.recipe.VanillaTagRecipeFixImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WeaponRecipes.class)
public class WeaponRecipesMixin {
    @WrapWithCondition(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V"
            )
    )
    private boolean stationapi_tagifyShapedRecipes(
            CraftingRecipeManager registry, ItemStack result, Object[] ingredientMap
    ) {
        boolean tagified = VanillaTagRecipeFixImpl.tagifyIngredients(ingredientMap);
        if (tagified) CraftingRegistry.addShapedRecipe(result, ingredientMap);
        return !tagified;
    }
}
