package net.modificationstation.stationapi.mixin.flattening.server;

import net.minecraft.block.BlockBase;
import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.packet.play.BlockChange0x35S2CPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.maths.TilePos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.packet.FlattenedBlockChangeS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(class_70.class)
public class Mixinclass_70 {

    @Shadow private ServerLevel field_2310;
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
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg) {
        return field_2310.getBlockState(field_2318, field_2319, field_2320).calcBlockBreakingDelta(arg, field_2310, new TilePos(field_2318, field_2319, field_2320));
    }

    @Redirect(
            method = "method_1830(IIII)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"
            )
    )
    private float getHardnessPerMeta2(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        return field_2310.getBlockState(i, j, k).calcBlockBreakingDelta(arg, field_2310, new TilePos(i, j, k));
    }

    @Redirect(
            method = "method_1829(III)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"
            )
    )
    private float getHardnessPerMeta3(BlockBase blockBase, PlayerBase arg, int i, int j, int k) {
        return field_2310.getBlockState(i, j, k).calcBlockBreakingDelta(arg, field_2310, new TilePos(i, j, k));
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
    private boolean canRemoveBlock(PlayerBase playerBase, BlockBase arg) {
        return playerBase.canHarvest(stationapi_method_1834_state);
    }

    @Redirect(
            method = "method_1834(III)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockBase;afterBreak(Lnet/minecraft/level/Level;Lnet/minecraft/entity/player/PlayerBase;IIII)V"
            )
    )
    private void redirectAfterBreak(BlockBase block, Level level, PlayerBase player, int x, int y, int z, int meta) {
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
    private BlockChange0x35S2CPacket flatten(int x, int y, int z, Level world) {
        return new FlattenedBlockChangeS2CPacket(x, y, z, world);
    }
    
    @ModifyArgs(method = "method_1834", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/server/level/ServerLevel;playLevelEvent(Lnet/minecraft/entity/player/PlayerBase;IIIII)V"
    ))
    private void changeIDStorage(Args args) {
        int data = this.field_2310.getTileId(args.get(2), args.get(3), args.get(4)) << 4;
        data |= this.field_2310.getTileMeta(args.get(2), args.get(3), args.get(4)) & 15;
        args.set(5, data);
    }
}
