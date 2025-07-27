package net.modificationstation.stationapi.mixin.flattening.client;

import net.minecraft.block.Block;
import net.minecraft.client.InteractionManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.SingleplayerInteractionManager;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleplayerInteractionManager.class)
class SingleplayerInteractionManagerMixin extends InteractionManager {
    @Unique
    private BlockState stationapi_breakBlock_state;
    @Unique
    private final MutableBlockPos stationapi_blockPos = new MutableBlockPos();

    private SingleplayerInteractionManagerMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Redirect(
            method = {
                    "attackBlock(IIII)V",
                    "processBlockBreakingAction(IIII)V"
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
            method = "breakBlock(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/InteractionManager;breakBlock(IIII)Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void stationapi_cacheBlockState(int x, int y, int z, int distance, CallbackInfoReturnable<Boolean> cir) {
        stationapi_breakBlock_state = minecraft.world.getBlockState(x, y, z);
    }

    @Redirect(
            method = "breakBlock(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ClientPlayerEntity;canHarvest(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean stationapi_canRemoveBlock(ClientPlayerEntity abstractClientPlayer, Block arg, int i, int j, int k, int l) {
        return abstractClientPlayer.canHarvest(minecraft.world, stationapi_blockPos.set(i, j, k), stationapi_breakBlock_state);
    }

    @Redirect(
            method = "breakBlock(IIII)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;IIII)V"
            )
    )
    private void stationapi_redirectAfterBreak(Block block, World world, PlayerEntity player, int x, int y, int z, int meta) {
        block.afterBreak(world, player, x, y, z, stationapi_breakBlock_state, meta);
    }

    @Inject(
            method = "breakBlock(IIII)Z",
            at = @At("RETURN")
    )
    private void stationapi_clearCache(int x, int y, int z, int distance, CallbackInfoReturnable<Boolean> cir) {
        stationapi_breakBlock_state = null;
    }
}
