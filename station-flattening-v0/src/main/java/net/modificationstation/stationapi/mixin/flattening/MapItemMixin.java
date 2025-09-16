package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.item.MapItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MapItem.class)
class MapItemMixin {
    @ModifyExpressionValue(
            method = "update",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=256"
            )
    )
    private int stationapi_adjustArray(int constant) {
        return Block.BLOCKS.length;
    }
}
