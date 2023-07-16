package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_467;
import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(class_467.class)
public class MixinClass467 {
    @ModifyConstant(
            method = {
                    "method_1531",
                    "method_1532"
            },
            constant = @Constant(intValue = 127)
    )
    private int stationapi_changeInclusiveTopY(int constant, Level world, EntityBase entity) {
        return world.getTopY() - 1;
    }

    @ModifyConstant(
            method = {
                    "method_1531",
                    "method_1532"
            },
            constant = @Constant(
                    expandZeroConditions = Constant.Condition.LESS_THAN_ZERO,
                    ordinal = 0
            )
    )
    private int stationapi_changeBottomY1(int constant, Level world, EntityBase entity) {
        return world.getBottomY();
    }

    @ModifyConstant(
            method = "method_1532",
            constant = @Constant(
                    expandZeroConditions = Constant.Condition.LESS_THAN_ZERO,
                    ordinal = 3
            )
    )
    private int stationapi_changeBottomY2(int constant, Level world, EntityBase entity) {
        return world.getBottomY();
    }

    @ModifyConstant(
            method = "method_1532",
            constant = @Constant(intValue = 70)
    )
    private int stationapi_changeNearMidY(int constant, Level world, EntityBase entity) {
        int topY = world.getTopY();
        return topY > constant + 10 ? constant : topY >> 1;
    }

    @ModifyConstant(
            method = "method_1532",
            constant = @Constant(intValue = 118)
    )
    private int stationapi_changeNearTopY(int constant, Level world, EntityBase entity) {
        return world.getTopY() - 10;
    }
}
