package net.modificationstation.stationapi.mixin.flattening;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Piston;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.tileentity.TileEntityPiston;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.block.StationFlatteningMovingPistonImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Piston.class)
public class PistonBlockMixin {
    @Unique
    private BlockState stationapi_pushedBlockState;

    @Inject(
            method = "onTileAction",
            at = @At("HEAD")
    )
    private void stationapi_getPushedBlockState1(Level world, int x, int y, int z, int direction, int meta, CallbackInfo ci) {
        stationapi_pushedBlockState = world.getBlockState(x, y, z);
    }

    @Inject(
            method = "onTileAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;getTileId(III)I"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_getPushedBlockState2(
            Level world, int x, int y, int z, int direction, int meta, CallbackInfo ci,
            int var7, TileEntityBase var8, int pushedX, int pushedY, int pushedZ
    ) {
        stationapi_pushedBlockState = world.getBlockState(pushedX, pushedY, pushedZ);
    }

    @Inject(
            method = "onTileAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/tileentity/TileEntityPiston;method_1518()I"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_getPushedBlockState3(
            Level world, int x, int y, int z, int direction, int meta, CallbackInfo ci,
            int var7, TileEntityBase var8, int pushedX, int pushedY, int pushedZ, int var12, int var13, int var14, TileEntityBase var15, TileEntityPiston pistonBlockEntity
    ) {
        stationapi_pushedBlockState = pistonBlockEntity.getPushedBlockState();
    }

    @Inject(
            method = "onTileAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/MovingPiston;method_1533(IIIZZ)Lnet/minecraft/tileentity/TileEntityBase;"
            )
    )
    private void stationapi_passPushedBlockState1(Level world, int x, int y, int z, int direction, int meta, CallbackInfo ci) {
        StationFlatteningMovingPistonImpl.pushedBlockState = stationapi_pushedBlockState;
    }

    @Inject(
            method = "method_766",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/level/Level;getTileId(III)I",
                    ordinal = 1
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void stationapi_getPushedBlockState4(
            Level world, int x, int y, int z, int direction, CallbackInfoReturnable<Boolean> cir,
            int var6, int var7, int var8, int pushedX, int pushedY, int pushedZ
    ) {
        stationapi_pushedBlockState = world.getBlockState(pushedX, pushedY, pushedZ);
    }

    @Inject(
            method = "method_766",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/MovingPiston;method_1533(IIIZZ)Lnet/minecraft/tileentity/TileEntityBase;",
                    ordinal = 0
            )
    )
    private void stationapi_passPushedBlockState2(Level world, int x, int y, int z, int direction, CallbackInfoReturnable<Boolean> cir) {
        StationFlatteningMovingPistonImpl.pushedBlockState = BlockBase.PISTON_HEAD.getDefaultState();
    }

    @Inject(
            method = "method_766",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/MovingPiston;method_1533(IIIZZ)Lnet/minecraft/tileentity/TileEntityBase;",
                    ordinal = 1
            )
    )
    private void stationapi_passPushedBlockState3(Level world, int x, int y, int z, int direction, CallbackInfoReturnable<Boolean> cir) {
        StationFlatteningMovingPistonImpl.pushedBlockState = stationapi_pushedBlockState;
    }
}
