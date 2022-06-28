package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.block.BlockBase;
import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.modificationstation.stationapi.api.block.AfterBreakWithBlockState;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_70.class)
public class Mixinclass_70 {

    @Shadow private ServerLevel field_2310;
    private BlockState stationapi$curBlockState;

    @Inject(
            method = "method_1834(III)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_70;method_1833(III)Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void cacheBlockState(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        stationapi$curBlockState = ((BlockStateView) field_2310).getBlockState(x, y, z);
    }

    @Redirect(
            method = "method_1834(III)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;afterBreak(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;IIII)V"
            )
    )
    private void redirectAfterBreak(BlockBase block, Level level, PlayerBase player, int x, int y, int z, int meta) {
        ((AfterBreakWithBlockState) block).afterBreak(level, player, x, y, z, stationapi$curBlockState, meta);
    }

    @Inject(
            method = "method_1834(III)Z",
            at = @At("RETURN")
    )
    private void clearCache(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        stationapi$curBlockState = null;
    }
}
