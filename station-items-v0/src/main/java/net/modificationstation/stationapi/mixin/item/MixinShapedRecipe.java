package net.modificationstation.stationapi.mixin.item;

import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShapedRecipe.class)
public class MixinShapedRecipe {

    @Shadow private ItemInstance output;

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "craft(Lnet/minecraft/inventory/Crafting;)Lnet/minecraft/item/ItemInstance;",
            at = @At(
                    value = "NEW",
                    target = "(III)Lnet/minecraft/item/ItemInstance;"
            )
    )
    private ItemInstance whyAreYouLikeThisWhenThereIsLiterallyACopyMethod(int itemId, int count, int damage) {
        return output.copy();
    }
}
