package net.modificationstation.stationapi.mixin.vanillafix.item.coal;

import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.vanillafix.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeRegistry.class)
public class MixinRecipeRegistry {

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;coal:Lnet/minecraft/item/ItemBase;"
            )
    )
    private ItemBase failsafe() {
        return Items.COAL;
    }

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V",
                    ordinal = 25
            )
    )
    private void stopRegisteringTorchRecipe(RecipeRegistry instance, ItemInstance item, Object[] objects) {}

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V",
                    ordinal = 26
            )
    )
    private void stopRegisteringCharcoalTorchRecipe(RecipeRegistry instance, ItemInstance item, Object[] objects) {}
}
