package net.modificationstation.stationapi.mixin.vanillafix.item.dye;

import net.minecraft.item.ItemBase;
import net.minecraft.recipe.MaterialBlockRecipes;
import net.modificationstation.stationapi.api.vanillafix.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MaterialBlockRecipes.class)
public class MixinMaterialBlockRecipes {

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;dyePowder:Lnet/minecraft/item/ItemBase;"
            )
    )
    private ItemBase getLapis() {
        return Items.LAPIS_LAZULI;
    }

    @ModifyConstant(
            method = "<init>()V",
            constant = @Constant(
                    intValue = 4,
                    ordinal = 1
            )
    )
    private int fixMeta(int constant) {
        return 0;
    }
}
