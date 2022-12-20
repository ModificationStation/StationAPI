package net.modificationstation.stationapi.mixin.vanillafix.block.dispenser;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeRegistry.class)
public class MixinRecipeRegistry {

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/block/BlockBase;I)Lnet/minecraft/item/ItemInstance;",
                    ordinal = 54
            )
    )
    private ItemInstance failsafe(BlockBase block, int count) {
        return new ItemInstance(0, 0, 0);
    }

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V",
                    ordinal = 53
            )
    )
    private void stopRegisteringRecipe(RecipeRegistry instance, ItemInstance result, Object[] ingredients) {}
}
