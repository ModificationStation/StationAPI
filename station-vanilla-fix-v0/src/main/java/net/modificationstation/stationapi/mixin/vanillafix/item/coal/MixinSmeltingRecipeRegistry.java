package net.modificationstation.stationapi.mixin.vanillafix.item.coal;

import net.minecraft.item.ItemBase;
import net.minecraft.recipe.SmeltingRecipeRegistry;
import net.modificationstation.stationapi.api.vanillafix.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SmeltingRecipeRegistry.class)
public class MixinSmeltingRecipeRegistry {

    @Redirect(
            method = "<init>()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;coal:Lnet/minecraft/item/ItemBase;"
            )
    )
    private ItemBase failsafe() {
        return Items.CHARCOAL;
    }
}
