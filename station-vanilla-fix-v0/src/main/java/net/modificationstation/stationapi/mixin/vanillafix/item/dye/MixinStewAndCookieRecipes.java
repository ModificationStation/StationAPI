package net.modificationstation.stationapi.mixin.vanillafix.item.dye;

import net.minecraft.item.ItemBase;
import net.minecraft.recipe.StewAndCookieRecipes;
import net.modificationstation.stationapi.api.vanillafix.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StewAndCookieRecipes.class)
public class MixinStewAndCookieRecipes {

    @Redirect(
            method = "register(Lnet/minecraft/recipe/RecipeRegistry;)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;dyePowder:Lnet/minecraft/item/ItemBase;"
            )
    )
    private ItemBase getCocoaBeans() {
        return Items.COCOA_BEANS;
    }

    @ModifyConstant(
            method = "register(Lnet/minecraft/recipe/RecipeRegistry;)V",
            constant = @Constant(
                    intValue = 3,
                    ordinal = 2
            )
    )
    private int fixMeta(int constant) {
        return 0;
    }
}
