package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SinglePlayerClientInteractionManager;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.AfterBreakWithBlockState;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.level.BlockStateView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePlayerClientInteractionManager.class)
public class MixinSinglePlayerClientInteractionManager extends BaseClientInteractionManager {

    private BlockState stationapi$curBlockState;

    public MixinSinglePlayerClientInteractionManager(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(
            method = "method_1716(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/BaseClientInteractionManager;method_1716(IIII)Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void cacheBlockState(int x, int y, int z, int distance, CallbackInfoReturnable<Boolean> cir) {
        stationapi$curBlockState = ((BlockStateView) minecraft.level).getBlockState(x, y, z);
    }

    @Redirect(
            method = "method_1716(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;afterBreak(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;IIII)V"
            )
    )
    private void redirectAfterBreak(BlockBase block, Level level, PlayerBase player, int x, int y, int z, int meta) {
        ((AfterBreakWithBlockState) block).afterBreak(level, player, x, y, z, stationapi$curBlockState, meta);
    }

    @Inject(
            method = "method_1716(IIII)Z",
            at = @At("RETURN")
    )
    private void clearCache(int x, int y, int z, int distance, CallbackInfoReturnable<Boolean> cir) {
        stationapi$curBlockState = null;
    }
}
