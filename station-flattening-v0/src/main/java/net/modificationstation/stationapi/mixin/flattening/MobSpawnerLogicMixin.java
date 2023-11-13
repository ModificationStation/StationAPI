package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.class_567;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.impl.world.StationDimension;
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
    @Unique private static World currentWorld;

    @Inject(
            method = "method_1869",
            at = @At("HEAD")
    )
    private static void stationapi_method_1869(World world, List<?> list, CallbackInfoReturnable<Boolean> info) {
        currentWorld = world;
    }

    @Inject(
            method = "method_1868",
            at = @At("HEAD")
    )
    private static void stationapi_method_1869(World world, int px, int pz, CallbackInfoReturnable<BlockPos> info) {
        currentWorld = world;
    }

    @ModifyConstant(method = {
            "method_1869",
            "method_1868"
    }, constant = @Constant(intValue = 128))
    private static int stationapi_changeMaxHeight(int value) {
        return stationapi_getWorldHeight(currentWorld);
    }

    @Unique
    private static int stationapi_getWorldHeight(World world) {
        StationDimension dimension = (StationDimension) world.dimension;
        return dimension.getActualWorldHeight();
    }
}
