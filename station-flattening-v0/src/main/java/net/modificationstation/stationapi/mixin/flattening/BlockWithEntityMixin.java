package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BlockWithEntity.class)
class BlockWithEntityMixin extends Block {
    private BlockWithEntityMixin(int i, Material arg) {
        super(i, arg);
    }

    @ModifyVariable(
            method = "<init>(ILnet/minecraft/block/Material;)V",
            index = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;<init>(ILnet/minecraft/block/Material;)V",
                    shift = At.Shift.AFTER
            ),
            argsOnly = true
    )
    private int stationapi_modifyId1(int value) {
        return id;
    }

    @ModifyVariable(
            method = "<init>(IILnet/minecraft/block/Material;)V",
            index = 1,
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;<init>(IILnet/minecraft/block/Material;)V",
                    shift = At.Shift.AFTER
            ),
            argsOnly = true
    )
    private int stationapi_modifyId2(int value) {
        return id;
    }
}
