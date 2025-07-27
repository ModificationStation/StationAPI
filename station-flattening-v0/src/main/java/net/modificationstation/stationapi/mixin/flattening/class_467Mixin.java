package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PortalForcer.class)
class class_467Mixin {
    @ModifyConstant(
            method = {
                    "method_1531",
                    "method_1532"
            },
            constant = @Constant(intValue = 127)
    )
    private int stationapi_changeInclusiveTopY(int constant, World world, Entity entity) {
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
    private int stationapi_changeBottomY1(int constant, World world, Entity entity) {
        return world.getBottomY();
    }

    @ModifyConstant(
            method = "method_1532",
            constant = @Constant(
                    expandZeroConditions = Constant.Condition.LESS_THAN_ZERO,
                    ordinal = 3
            )
    )
    private int stationapi_changeBottomY2(int constant, World world, Entity entity) {
        return world.getBottomY();
    }

    @ModifyConstant(
            method = "method_1532",
            constant = @Constant(intValue = 70)
    )
    private int stationapi_changeNearMidY(int constant, World world, Entity entity) {
        int topY = world.getTopY();
        return topY > constant + 10 ? constant : topY >> 1;
    }

    @ModifyConstant(
            method = "method_1532",
            constant = @Constant(intValue = 118)
    )
    private int stationapi_changeNearTopY(int constant, World world, Entity entity) {
        return world.getTopY() - 10;
    }
}
