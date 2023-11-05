package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_567;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(class_567.class)
class MobSpawnerLogicMixin {
    @Unique private static World currentLevel;

    @Inject(
            method = "method_1869",
            at = @At("HEAD")
    )
    private static void stationapi_method_1869(World level, List<?> list, CallbackInfoReturnable<Boolean> info) {
        currentLevel = level;
    }

    @Inject(
            method = "method_1868",
            at = @At("HEAD")
    )
    private static void stationapi_method_1869(World level, int px, int pz, CallbackInfoReturnable<BlockPos> info) {
        currentLevel = level;
    }

    @ModifyConstant(method = {
            "method_1869",
            "method_1868"
    }, constant = @Constant(intValue = 128))
    private static int stationapi_changeMaxHeight(int value) {
        return stationapi_getLevelHeight(currentLevel);
    }

    @Unique
    private static int stationapi_getLevelHeight(World level) {
        StationDimension dimension = (StationDimension) level.dimension;
        return dimension.getActualLevelHeight();
    }
}
