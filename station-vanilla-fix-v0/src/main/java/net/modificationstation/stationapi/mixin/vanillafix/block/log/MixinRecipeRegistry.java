package net.modificationstation.stationapi.mixin.vanillafix.block.log;

import net.minecraft.item.ItemInstance;
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
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V",
                    ordinal = 23
            )
    )
    private void stopPlanksRecipeRegister(RecipeRegistry instance, ItemInstance itemInstance, Object[] objects) {}
}
