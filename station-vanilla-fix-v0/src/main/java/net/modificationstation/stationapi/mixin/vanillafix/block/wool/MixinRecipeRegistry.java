package net.modificationstation.stationapi.mixin.vanillafix.block.wool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RecipeRegistry.class)
public class MixinRecipeRegistry {

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;WOOL:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC,
                    ordinal = 0
            )
    )
    private BlockBase getWoolForStringToWoolRecipe() {
        return Blocks.WHITE_WOOL;
    }

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V",
                    ordinal = 42
            )
    )
    private void stopPaintingRecipe(RecipeRegistry instance, ItemInstance item, Object[] objects) {}

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapedRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V",
                    ordinal = 56
            )
    )
    private void stopBedRecipe(RecipeRegistry instance, ItemInstance item, Object[] objects) {}
}
