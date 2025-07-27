package net.modificationstation.stationapi.mixin.flattening;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.World;
import net.minecraft.world.chunk.light.LightUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightUpdate.class)
class class_417Mixin {
    @Unique private short maxBlock;
    @Unique private short minBlock;

    @Inject(
            method = "updateLight(Lnet/minecraft/world/World;)V",
            at = @At("HEAD")
    )
    private void stationapi_spawnMonstersAndWakePlayers(World world, CallbackInfo info) {
        maxBlock = (short) world.getTopY();
        minBlock = (short) world.getBottomY();
    }

    @ModifyConstant(
            method = "updateLight(Lnet/minecraft/world/World;)V",
            constant = @Constant(
                    expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO,
                    ordinal = 0
            )
    )
    private int stationapi_changeMinHeight(int value) {
        return minBlock;
    }

    @ModifyConstant(
            method = "updateLight(Lnet/minecraft/world/World;)V",
            constant = @Constant(
                    intValue = 0,
                    ordinal = 7
            )
    )
    private int stationapi_changeMinHeightFallback(int value) {
        return minBlock;
    }

    @ModifyConstant(
            method = "updateLight(Lnet/minecraft/world/World;)V",
            constant = @Constant(intValue = 128)
    )
    private int stationapi_changeMaxHeight(int value) {
        return maxBlock;
    }

    @ModifyConstant(
            method = "updateLight(Lnet/minecraft/world/World;)V",
            constant = @Constant(intValue = 127)
    )
    private int stationapi_changeMaxHeightFallback(int value) {
        return maxBlock - 1;
    }

    @ModifyVariable(
            method = "updateLight(Lnet/minecraft/world/World;)V",
            at = @At(
                    value = "STORE",
                    ordinal = 2
            ),
            index = 20
    )
    private int stationapi_getStateLuminance(int original, @Local World world, @Local(index = 10) int x, @Local(index = 15) int y, @Local(index = 11) int z) {
        return world.getBlockState(x, y, z).getLuminance();
    }
}
