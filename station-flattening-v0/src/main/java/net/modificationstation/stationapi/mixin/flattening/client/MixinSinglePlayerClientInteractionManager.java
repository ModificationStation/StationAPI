package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.BaseClientInteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SinglePlayerClientInteractionManager;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SinglePlayerClientInteractionManager.class)
public class MixinSinglePlayerClientInteractionManager extends BaseClientInteractionManager {

    @Unique
    private BlockState stationapi_method_1716_state;

    public MixinSinglePlayerClientInteractionManager(Minecraft minecraft) {
        super(minecraft);
    }

    @Redirect(
            method = {
                    "method_1707(IIII)V",
                    "method_1721(IIII)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"
            )
    )
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        return minecraft.level.getBlockState(i, j, k).calcBlockBreakingDelta(arg, minecraft.level, new TilePos(i, j, k));
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
        stationapi_method_1716_state = minecraft.level.getBlockState(x, y, z);
    }

    @Redirect(method = "method_1716(IIII)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/AbstractClientPlayer;canRemoveBlock(Lnet/minecraft/block/BlockBase;)Z"))
    private boolean canRemoveBlock(AbstractClientPlayer abstractClientPlayer, BlockBase arg) {
        return abstractClientPlayer.canHarvest(stationapi_method_1716_state);
    }

    @Redirect(
            method = "method_1716(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;afterBreak(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;IIII)V"
            )
    )
    private void redirectAfterBreak(BlockBase block, Level level, PlayerBase player, int x, int y, int z, int meta) {
        block.afterBreak(level, player, x, y, z, stationapi_method_1716_state, meta);
    }

    @Inject(
            method = "method_1716(IIII)Z",
            at = @At("RETURN")
    )
    private void clearCache(int x, int y, int z, int distance, CallbackInfoReturnable<Boolean> cir) {
        stationapi_method_1716_state = null;
    }
}
