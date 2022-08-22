package net.modificationstation.stationapi.mixin.vanillafix.item.dye;

import net.minecraft.entity.swimming.Squid;
import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.vanillafix.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Squid.class)
public class MixinSquid {

    @Redirect(
            method = "getDrops()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;dyePowder:Lnet/minecraft/item/ItemBase;"
            )
    )
    private ItemBase getInkSac() {
        return Items.INK_SAC;
    }
}
