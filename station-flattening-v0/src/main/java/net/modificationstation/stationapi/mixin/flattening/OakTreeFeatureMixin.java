package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_543;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.Random;

@Mixin(class_543.class)
class OakTreeFeatureMixin {
    @ModifyConstant(
            method = "method_1142",
            constant = @Constant(
                    intValue = 1,
                    ordinal = 1
            )
    )
    private int stationapi_changeBottomYPlus1(int constant, World world, Random random, int x, int y, int z) {
        return world.getBottomY() + 1;
    }

    @ModifyConstant(
            method = "method_1142",
            constant = @Constant(intValue = 128)
    )
    private int stationapi_changeMaxHeight(int constant, World world, Random random, int x, int y, int z) {
        return world.getTopY();
    }

    @ModifyConstant(
            method = "method_1142",
            constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO)
    )
    private int stationapi_changeBottomYLT(int constant, World world, Random random, int x, int y, int z) {
        return world.getBottomY();
    }
}
