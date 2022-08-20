package net.modificationstation.stationapi.mixin.vanillafix.block.log;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.objectweb.asm.Opcodes.GETFIELD;

@Mixin(SmeltingRecipeRegistry.class)
public class MixinSmeltingRecipeRegistry {

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;id:I",
                    opcode = GETFIELD,
                    ordinal = 6
            )
    )
    private int redirectId(BlockBase instance) {
        return 0;
    }

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/recipe/SmeltingRecipeRegistry;addSmeltingRecipe(ILnet/minecraft/item/ItemInstance;)V",
                    ordinal = 9
            )
    )
    private void stopRegisteringCharcoalRecipe(SmeltingRecipeRegistry instance, int arg, ItemInstance itemInstance) {}
}
