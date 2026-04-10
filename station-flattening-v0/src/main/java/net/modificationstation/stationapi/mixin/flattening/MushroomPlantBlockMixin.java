package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MushroomPlantBlock.class)
class MushroomPlantBlockMixin {
    @ModifyConstant(
            method = "canGrow",
            constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO)
    )
    private int stationapi_changeBottomYCheck(
            int constant,
            @Local(index = 1, argsOnly = true) World world
    ) {
        return world.getBottomY();
    }

    @ModifyExpressionValue(
            method = "canGrow",
            at = @At(
                    value = "CONSTANT",
                    args = "intValue=128"
            )
    )
    private int stationapi_changeTopYCheck(
            int original,
            @Local(index = 1, argsOnly = true) World world
    ) {
        return world.getTopY();
    }
}
