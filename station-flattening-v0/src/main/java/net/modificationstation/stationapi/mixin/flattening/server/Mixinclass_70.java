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
public class Mixinclass_70 {

    @Shadow private class_73 field_2310;
    @Shadow private int field_2318;
    @Shadow private int field_2319;
    @Shadow private int field_2320;

    @Unique
    private BlockState stationapi_method_1834_state;

    @Redirect(
            method = "method_1828()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"
            )
    )
    private float getHardnessPerMeta(Block blockBase, PlayerEntity arg) {
        return field_2310.getBlockState(field_2318, field_2319, field_2320).calcBlockBreakingDelta(arg, field_2310, new BlockPos(field_2318, field_2319, field_2320));
    }

    @Redirect(
            method = "method_1830(IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"
            )
    )
    private float getHardnessPerMeta2(Block blockBase, PlayerEntity arg, int i, int j, int k, int i1) {
        return field_2310.getBlockState(i, j, k).calcBlockBreakingDelta(arg, field_2310, new BlockPos(i, j, k));
    }

    @Redirect(
            method = "method_1829(III)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"
            )
    )
    private float getHardnessPerMeta3(Block blockBase, PlayerEntity arg, int i, int j, int k) {
        return field_2310.getBlockState(i, j, k).calcBlockBreakingDelta(arg, field_2310, new BlockPos(i, j, k));
    }

    @Inject(
            method = "method_1834(III)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/class_70;method_1833(III)Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void cacheBlockState(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        stationapi_method_1834_state = field_2310.getBlockState(x, y, z);
    }

    @Redirect(
            method = "method_1834(III)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerBase;canRemoveBlock(Lnet/minecraft/block/BlockBase;)Z"
            )
    )
    private boolean canRemoveBlock(PlayerEntity playerBase, Block arg) {
        return playerBase.canHarvest(stationapi_method_1834_state);
    }

    @Redirect(
            method = "method_1834(III)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;afterBreak(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;IIII)V"
            )
    )
    private void redirectAfterBreak(Block block, World level, PlayerEntity player, int x, int y, int z, int meta) {
        block.afterBreak(level, player, x, y, z, stationapi_method_1834_state, meta);
    }

    @Inject(
            method = "method_1834(III)Z",
            at = @At("RETURN")
    )
    private void clearCache(int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        stationapi_method_1834_state = null;
    }

    @Redirect(
            method = "method_1834",
            at = @At(
                    value = "NEW",
                    target = "(IIILnet/minecraft/level/Level;)Lnet/minecraft/packet/play/BlockChange0x35S2CPacket;"
            )
    )
    private BlockUpdateS2CPacket flatten(int x, int y, int z, World world) {
        return new FlattenedBlockChangeS2CPacket(x, y, z, world);
    }
    
    @ModifyConstant(method = "method_1834", constant = @Constant(intValue = 256))
    private int changeMetaShift(int value) {
        return 268435456;
    }
}
