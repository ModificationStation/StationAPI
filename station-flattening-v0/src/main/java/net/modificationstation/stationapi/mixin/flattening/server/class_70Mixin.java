package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.block.Block;
import net.minecraft.class_70;
import net.minecraft.class_73;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_70.class)
class class_70Mixin {
    @Shadow private class_73 field_2310;
    @Shadow private int field_2318;
    @Shadow private int field_2319;
    @Shadow private int field_2320;

    @Unique
    private BlockState stationapi_method_1834_state;

    @Redirect(
            method = "method_1828",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"
            )
    )
    private float stationapi_getHardnessPerMeta(Block block, PlayerEntity arg) {
        return field_2310.getBlockState(field_2318, field_2319, field_2320).calcBlockBreakingDelta(arg, field_2310, new BlockPos(field_2318, field_2319, field_2320));
    }

    @Redirect(
            method = "method_1830",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"
            )
    )
    private float stationapi_getHardnessPerMeta2(Block block, PlayerEntity arg, int i, int j, int k, int i1) {
        return field_2310.getBlockState(i, j, k).calcBlockBreakingDelta(arg, field_2310, new BlockPos(i, j, k));
    }

    @Redirect(
            method = "method_1829",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;getHardness(Lnet/minecraft/entity/player/PlayerEntity;)F"
            )
    )
    private float stationapi_getHardnessPerMeta3(Block block, PlayerEntity arg, int i, int j, int k) {
        return field_2310.getBlockState(i, j, k).calcBlockBreakingDelta(arg, field_2310, new BlockPos(i, j, k));
    }

    @Inject(
            method = "method_1834",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_70;method_1833(III)Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void stationapi_cacheBlockState(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        stationapi_method_1834_state = field_2310.getBlockState(x, y, z);
    }

    @Redirect(
            method = "method_1834",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;method_514(Lnet/minecraft/block/Block;)Z"
            )
    )
    private boolean stationapi_canRemoveBlock(PlayerEntity playerBase, Block arg) {
        return playerBase.canHarvest(stationapi_method_1834_state);
    }

    @Redirect(
            method = "method_1834",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/Block;afterBreak(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;IIII)V"
            )
    )
    private void stationapi_redirectAfterBreak(Block block, World world, PlayerEntity player, int x, int y, int z, int meta) {
        block.afterBreak(world, player, x, y, z, stationapi_method_1834_state, meta);
    }

    @Inject(
            method = "method_1834",
            at = @At("RETURN")
    )
    private void stationapi_clearCache(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        stationapi_method_1834_state = null;
    }

    @Redirect(
            method = "method_1834",
            at = @At(
                    value = "NEW",
                    target = "(IIILnet/minecraft/world/World;)Lnet/minecraft/network/packet/s2c/play/BlockUpdateS2CPacket;"
            )
    )
    private BlockUpdateS2CPacket stationapi_flatten(int x, int y, int z, World world) {
        return new FlattenedBlockChangeS2CPacket(x, y, z, world);
    }
    
    @ModifyConstant(
            method = "method_1834",
            constant = @Constant(intValue = 256)
    )
    private int stationapi_changeMetaShift(int value) {
        return 268435456;
    }
}
