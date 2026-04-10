package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.DoorBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DoorBlock.class)
class DoorBlockMixin {
    @ModifyExpressionValue(
            method = "canPlaceAt",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=127"
            )
    )
    private int stationapi_changeTopYCheck(
            int original,
            @Local(index = 1, argsOnly = true) World world
    ) {
        return world.getTopY() - 1;
    }
}
