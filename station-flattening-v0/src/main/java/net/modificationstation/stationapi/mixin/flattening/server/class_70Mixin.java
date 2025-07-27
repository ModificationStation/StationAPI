package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
class class_70Mixin {
    @Shadow private ServerWorld world;
    @Shadow private int miningX;
    @Shadow private int miningY;
    @Shadow private int miningZ;

    @Unique
    private BlockState stationapi_tryBreakBlock_state;
    @Unique
    private final MutableBlockPos stationapi_blockPos = new MutableBlockPos();

    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"
            )
    )
    private float stationapi_getHardnessPerMeta(Block block, PlayerEntity arg) {
        return world.getBlockState(miningX, miningY, miningZ).calcBlockBreakingDelta(arg, world, new BlockPos(miningX, miningY, miningZ));
    }

    @Redirect(
            method = "onBlockBreakingAction",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"
            )
    )
    private float stationapi_getHardnessPerMeta2(Block block, PlayerEntity arg, int i, int j, int k, int i1) {
        return world.getBlockState(i, j, k).calcBlockBreakingDelta(arg, world, new BlockPos(i, j, k));
    }

    @Redirect(
            method = "continueMining",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"
            )
    )
    private float stationapi_getHardnessPerMeta3(Block block, PlayerEntity arg, int i, int j, int k) {
        return world.getBlockState(i, j, k).calcBlockBreakingDelta(arg, world, new BlockPos(i, j, k));
    }

    @Inject(
            method = "tryBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_70;finishMining(III)Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void stationapi_cacheBlockState(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        stationapi_tryBreakBlock_state = world.getBlockState(x, y, z);
    }

    @Redirect(
            method = "tryBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;canHarvest(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean stationapi_canRemoveBlock(PlayerEntity playerBase, Block arg, int i, int j, int k) {
        return playerBase.canHarvest(world, stationapi_blockPos.set(i, j, k), stationapi_tryBreakBlock_state);
    }

    @Redirect(
            method = "tryBreakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;IIII)V"
            )
    )
    private void stationapi_redirectAfterBreak(Block block, World world, PlayerEntity player, int x, int y, int z, int meta) {
        block.afterBreak(world, player, x, y, z, stationapi_tryBreakBlock_state, meta);
    }

    @Inject(
            method = "tryBreakBlock",
            at = @At("RETURN")
    )
    private void stationapi_clearCache(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        stationapi_tryBreakBlock_state = null;
    }

    @Redirect(
            method = "tryBreakBlock",
            at = @At(
                    value = "NEW",
                    target = "(IIILnet/minecraft/world/World;)Lnet/minecraft/network/packet/s2c/play/BlockUpdateS2CPacket;"
            )
    )
    private BlockUpdateS2CPacket stationapi_flatten(int x, int y, int z, World world) {
        return new FlattenedBlockChangeS2CPacket(x, y, z, world);
    }
    
    @ModifyConstant(
            method = "tryBreakBlock",
            constant = @Constant(intValue = 256)
    )
    private int stationapi_changeMetaShift(int value) {
        return 268435456;
    }
}
