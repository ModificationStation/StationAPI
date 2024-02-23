package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.OakTreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.Random;

@Mixin(OakTreeFeature.class)
class OakTreeFeatureMixin {
    @ModifyConstant(
            method = "generate",
            constant = @Constant(
                    intValue = 1,
                    ordinal = 1
            )
    )
    private int stationapi_changeBottomYPlus1(int constant, World world, Random random, int x, int y, int z) {
        return world.getBottomY() + 1;
    }

    @ModifyConstant(
            method = "generate",
            constant = @Constant(intValue = 128)
    )
    private int stationapi_changeMaxHeight(int constant, World world, Random random, int x, int y, int z) {
        return world.getTopY();
    }

    @ModifyConstant(
            method = "generate",
            constant = @Constant(expandZeroConditions = Constant.Condition.LESS_THAN_ZERO)
    )
    private int stationapi_changeBottomYLT(int constant, World world, Random random, int x, int y, int z) {
        return world.getBottomY();
    }
}
