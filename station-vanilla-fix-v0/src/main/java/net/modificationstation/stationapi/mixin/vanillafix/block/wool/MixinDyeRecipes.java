package net.modificationstation.stationapi.mixin.vanillafix.block.wool;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.DyeRecipes;
import net.minecraft.recipe.RecipeRegistry;
import net.modificationstation.stationapi.api.vanillafix.block.Blocks;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DyeRecipes.class)
public class MixinDyeRecipes {

    @Redirect(
            method = "register(Lnet/minecraft/recipe/RecipeRegistry;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;WOOL:Lnet/minecraft/block/BlockBase;",
                    opcode = Opcodes.GETSTATIC
            )
    )
    private BlockBase redirectWoolItemId() {
        return Blocks.WHITE_WOOL;
    }

    @Redirect(
            method = "register(Lnet/minecraft/recipe/RecipeRegistry;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/RecipeRegistry;addShapelessRecipe(Lnet/minecraft/item/ItemInstance;[Ljava/lang/Object;)V",
                    ordinal = 0
            )
    )
    private void redirectCraftingResult(RecipeRegistry instance, ItemInstance item, Object[] objects) {}
}
