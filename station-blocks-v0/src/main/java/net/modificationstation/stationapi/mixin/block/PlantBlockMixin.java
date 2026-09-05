package net.modificationstation.stationapi.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.PlantBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("LocalMayUseName")
@Mixin(PlantBlock.class)
class PlantBlockMixin {
    @WrapOperation(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PlantBlock;canPlantOnTop(I)Z"))
    protected boolean stationapi_injectCanPlantOnTopCanPlaceAt(PlantBlock plantBlock, int id, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 0) World world, @Local(argsOnly = true, ordinal = 0) int x, @Local(argsOnly = true, ordinal = 1) int y, @Local(argsOnly = true, ordinal = 2) int z) {
        return original.call(plantBlock, id);
    }

    @WrapOperation(method = "canGrow", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/PlantBlock;canPlantOnTop(I)Z"))
    protected boolean stationapi_injectCanPlantOnTopCanGrow(PlantBlock plantBlock, int id, Operation<Boolean> original, @Local(argsOnly = true, ordinal = 0) World world, @Local(argsOnly = true, ordinal = 0) int x, @Local(argsOnly = true, ordinal = 1) int y, @Local(argsOnly = true, ordinal = 2) int z) {
        return original.call(plantBlock, id);
    }
}
