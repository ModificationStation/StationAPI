package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.world.gen.carver.CaveCarver;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CaveCarver.class)
abstract class CaveGenMixin implements CaveGenBaseImpl {
    @ModifyConstant(
            method = "method_1400",
            constant = {
                    @Constant(intValue = 1, ordinal = 8),
                    @Constant(intValue = 1, ordinal = 9)
            }
    )
    private int stationapi_changeBottomYP1Cap(int constant) {
        return Math.max(stationapi_getWorld().getBottomY() + 1, constant);
    }

    @ModifyConstant(
            method = "method_1400",
            constant = @Constant(intValue = 120)
    )
    private int stationapi_changeTopYM8Cap(int constant) {
        return Math.min(stationapi_getWorld().getTopY() - 8, constant);
    }

    @ModifyConstant(
            method = "method_1400",
            constant = {
                    @Constant(intValue = 128, ordinal = 0),
                    @Constant(intValue = 128, ordinal = 2)
            }
    )
    private int stationapi_changeHeight(int constant) {
        return MathHelper.smallestEncompassingPowerOfTwo(stationapi_getWorld().getHeight());
    }

    @ModifyVariable(
            method = "method_1400",
            at = @At(value = "STORE", ordinal = 0),
            index = 43
    )
    private int stationapi_adjustForCustomDepth1(int value) {
        return value - stationapi_getWorld().getBottomY();
    }

    @ModifyVariable(
            method = "method_1400",
            at = @At(value = "STORE", ordinal = 0),
            index = 46
    )
    private int stationapi_adjustForCustomDepth2(int value) {
        return value - stationapi_getWorld().getBottomY();
    }
}
