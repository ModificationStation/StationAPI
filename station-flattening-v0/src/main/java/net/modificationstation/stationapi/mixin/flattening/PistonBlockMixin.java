package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.Block;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.class_283;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.block.StationFlatteningMovingPistonImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PistonBlock.class)
class PistonBlockMixin {
    @Unique
    private BlockState stationapi_pushedBlockState;

    @Inject(
            method = "onBlockAction",
            at = @At("HEAD")
    )
    private void stationapi_getPushedBlockState1(World world, int x, int y, int z, int direction, int meta, CallbackInfo ci) {
        stationapi_pushedBlockState = world.getBlockState(x, y, z);
    }

    @Inject(
            method = "onBlockAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockId(III)I"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_getPushedBlockState2(
            World world, int x, int y, int z, int direction, int meta, CallbackInfo ci,
            int var7, BlockEntity var8, int pushedX, int pushedY, int pushedZ
    ) {
        stationapi_pushedBlockState = world.getBlockState(pushedX, pushedY, pushedZ);
    }

    @Inject(
            method = "onBlockAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_283;method_1518()I"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_getPushedBlockState3(
            World world, int x, int y, int z, int direction, int meta, CallbackInfo ci,
            int var7, BlockEntity var8, int pushedX, int pushedY, int pushedZ, int var12, int var13, int var14, BlockEntity var15, class_283 pistonBlockEntity
    ) {
        stationapi_pushedBlockState = pistonBlockEntity.getPushedBlockState();
    }

    @Inject(
            method = "onBlockAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/PistonExtensionBlock;method_1533(IIIZZ)Lnet/minecraft/block/entity/BlockEntity;"
            )
    )
    private void stationapi_passPushedBlockState1(World world, int x, int y, int z, int direction, int meta, CallbackInfo ci) {
        StationFlatteningMovingPistonImpl.pushedBlockState = stationapi_pushedBlockState;
    }

    @Inject(
            method = "method_766",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockId(III)I",
                    ordinal = 1
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_getPushedBlockState4(
            World world, int x, int y, int z, int direction, CallbackInfoReturnable<Boolean> cir,
            int var6, int var7, int var8, int pushedX, int pushedY, int pushedZ
    ) {
        stationapi_pushedBlockState = world.getBlockState(pushedX, pushedY, pushedZ);
    }

    @Inject(
            method = "method_766",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/PistonExtensionBlock;method_1533(IIIZZ)Lnet/minecraft/block/entity/BlockEntity;",
                    ordinal = 0
            )
    )
    private void stationapi_passPushedBlockState2(World world, int x, int y, int z, int direction, CallbackInfoReturnable<Boolean> cir) {
        StationFlatteningMovingPistonImpl.pushedBlockState = Block.PISTON_HEAD.getDefaultState();
    }

    @Inject(
            method = "method_766",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/PistonExtensionBlock;method_1533(IIIZZ)Lnet/minecraft/block/entity/BlockEntity;",
                    ordinal = 1
            )
    )
    private void stationapi_passPushedBlockState3(World world, int x, int y, int z, int direction, CallbackInfoReturnable<Boolean> cir) {
        StationFlatteningMovingPistonImpl.pushedBlockState = stationapi_pushedBlockState;
    }
}
