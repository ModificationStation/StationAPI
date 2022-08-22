package net.modificationstation.stationapi.mixin.vanillafix.item.coal;

import net.minecraft.item.ItemBase;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntityFurnace.class)
public class MixinTileEntityFurnace {

    @Redirect(
            method = "getFuelTime",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/ItemBase;id:I",
                    ordinal = 2
            )
    )
    private int dontinvokeplsplspls(ItemBase instance) {
        return -1;
    }
}
