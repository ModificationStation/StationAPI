package net.modificationstation.stationapi.mixin.metablock.server;

import net.minecraft.block.BlockBase;
import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.server.level.ServerLevel;
import net.modificationstation.stationapi.api.block.PlayerBlockHardnessPerMeta;
import net.modificationstation.stationapi.api.entity.player.CanPlayerRemoveMetaBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_70.class)
public class Mixinclass_70 {

    @Shadow private ServerLevel field_2310;

    @Shadow private int field_2318;

    @Shadow private int field_2319;

    @Shadow private int field_2320;

    @Redirect(method = "method_1828()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg) {
        return ((PlayerBlockHardnessPerMeta) blockBase).getHardness(arg, field_2310.getTileMeta(field_2318, field_2319, field_2320));
    }

    @Redirect(method = "method_1830(IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta2(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        return ((PlayerBlockHardnessPerMeta) blockBase).getHardness(arg, field_2310.getTileMeta(i, j, k));
    }

    @Redirect(method = "method_1829(III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta3(BlockBase blockBase, PlayerBase arg, int i, int j, int k) {
        return ((PlayerBlockHardnessPerMeta) blockBase).getHardness(arg, field_2310.getTileMeta(i, j, k));
    }

    private int capturedMeta;

    @Inject(method = "method_1834(III)Z", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerLevel;getTileMeta(III)I", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureMeta(int i, int j, int k, CallbackInfoReturnable<Boolean> cir, int var4, int var5) {
        capturedMeta = var5;
    }

    @Redirect(method = "method_1834(III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerBase;canRemoveBlock(Lnet/minecraft/block/BlockBase;)Z"))
    private boolean canRemoveBlock(PlayerBase playerBase, BlockBase arg) {
        return ((CanPlayerRemoveMetaBlock) playerBase).canRemoveBlock(arg, capturedMeta);
    }
}
