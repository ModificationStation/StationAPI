package net.modificationstation.stationapi.mixin.vanillafix;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.DyeRecipes;
import net.modificationstation.stationapi.api.vanillafix.item.Items;
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
                    target = "Lnet/minecraft/block/BlockBase;id:I",
                    opcode = Opcodes.GETFIELD
            )
    )
    private int redirectWoolItemId(BlockBase instance) {
        return Items.WHITE_WOOL.id;
    }

    @SuppressWarnings({"InvalidMemberReference", "UnresolvedMixinReference", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
            method = "register(Lnet/minecraft/recipe/RecipeRegistry;)V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/block/BlockBase;II)Lnet/minecraft/item/ItemInstance;"
            )
    )
    private ItemInstance redirectCraftingResult(BlockBase block, int count, int meta) {
        return new ItemInstance(Items.woolMetaToBlock(meta), count);
    }
}
