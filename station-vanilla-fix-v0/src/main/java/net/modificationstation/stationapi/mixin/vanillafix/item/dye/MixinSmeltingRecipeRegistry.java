package net.modificationstation.stationapi.mixin.vanillafix.item.dye;

import net.minecraft.item.ItemBase;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationapi.api.vanillafix.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SmeltingRecipeRegistry.class)
public class MixinSmeltingRecipeRegistry {

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;dyePowder:Lnet/minecraft/item/ItemBase;"
            )
    )
    private ItemBase getGreenDye() {
        return Items.GREEN_DYE;
    }

    @ModifyConstant(
            method = "<init>()V",
            constant = @Constant(intValue = 2)
    )
    private int fixMeta(int constant) {
        return 0;
    }
}
