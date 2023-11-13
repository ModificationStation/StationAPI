package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.SingleplayerInteractionManager;
import net.minecraft.block.Block;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleplayerInteractionManager.class)
class SingleplayerInteractionManagerMixin extends InteractionManager {
    @Unique
    private BlockState stationapi_method_1716_state;

    private SingleplayerInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Redirect(
            method = {
                    "method_1707(IIII)V",
                    "method_1721(IIII)V"
            },
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"
            )
    )
    private float stationapi_getHardnessPerMeta(Block block, PlayerEntity arg, int i, int j, int k, int i1) {
        return minecraft.world.getBlockState(i, j, k).calcBlockBreakingDelta(arg, minecraft.world, new BlockPos(i, j, k));
    }

    @Inject(
            method = "method_1716(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/InteractionManager;method_1716(IIII)Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void stationapi_cacheBlockState(int x, int y, int z, int distance, CallbackInfoReturnable<Boolean> cir) {
        stationapi_method_1716_state = minecraft.world.getBlockState(x, y, z);
    }

    @Redirect(
            method = "method_1716(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ClientPlayerEntity;method_514(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean stationapi_canRemoveBlock(ClientPlayerEntity abstractClientPlayer, Block arg) {
        return abstractClientPlayer.canHarvest(stationapi_method_1716_state);
    }

    @Redirect(
            method = "method_1716(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;IIII)V"
            )
    )
    private void stationapi_redirectAfterBreak(Block block, World world, PlayerEntity player, int x, int y, int z, int meta) {
        block.afterBreak(world, player, x, y, z, stationapi_method_1716_state, meta);
    }

    @Inject(
            method = "method_1716(IIII)Z",
            at = @At("RETURN")
    )
    private void stationapi_clearCache(int x, int y, int z, int distance, CallbackInfoReturnable<Boolean> cir) {
        stationapi_method_1716_state = null;
    }
}
