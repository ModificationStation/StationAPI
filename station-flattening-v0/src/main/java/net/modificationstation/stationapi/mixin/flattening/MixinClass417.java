package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_417;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_417.class)
public class MixinClass417 {
    @Unique private short maxBlock;
    @Unique private short minBlock;

    @Inject(method = "method_1402(Lnet/minecraft/level/Level;)V", at = @At("HEAD"))
    private void method_1869(Level level, CallbackInfo info) {
        maxBlock = (short) level.getTopY();
        minBlock = (short) level.getBottomY();
    }

    @ModifyConstant(method = "method_1402(Lnet/minecraft/level/Level;)V", constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO, ordinal = 0))
    private int changeMinHeight(int value) {
        return minBlock;
    }

    @ModifyConstant(method = "method_1402(Lnet/minecraft/level/Level;)V", constant = @Constant(intValue = 0, ordinal = 7))
    private int changeMinHeightFallback(int value) {
        return minBlock;
    }

    @ModifyConstant(method = "method_1402(Lnet/minecraft/level/Level;)V", constant = @Constant(intValue = 128))
    private int changeMaxHeight(int value) {
        return maxBlock;
    }

    @ModifyConstant(method = "method_1402(Lnet/minecraft/level/Level;)V", constant = @Constant(intValue = 127))
    private int changeMaxHeightFallback(int value) {
        return maxBlock - 1;
    }

    @ModifyVariable(
            method = "method_1402(Lnet/minecraft/level/Level;)V",
            at = @At(
                    value = "STORE",
                    ordinal = 2
            ),
            index = 20
    )
    private int getStateLuminance(int original, @Local Level level, @Local(index = 10) int x, @Local(index = 15) int y, @Local(index = 11) int z) {
        return level.getBlockState(x, y, z).getLuminance();
    }
}
