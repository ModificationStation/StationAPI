package net.modificationstation.stationapi.mixin.vanillafix.block.sapling;

import net.minecraft.block.BlockBase;
import net.minecraft.tileentity.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TileEntityFurnace.class)
public class MixinTileEntityFurnace {

    @Redirect(
            method = "getFuelTime(Lnet/minecraft/item/ItemInstance;)I",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/block/BlockBase;id:I"
            )
    )
    private int plsDontEverInvokePlsPlsPlsPlsPls(BlockBase instance) {
        return -1;
    }
}
