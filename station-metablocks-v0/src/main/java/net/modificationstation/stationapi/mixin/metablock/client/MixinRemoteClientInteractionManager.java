package net.modificationstation.stationapi.mixin.metablock.client;

import net.minecraft.block.BlockBase;
import net.minecraft.client.RemoteClientInteractionManager;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.modificationstation.stationapi.api.block.PlayerBlockHardnessPerMeta;
import net.modificationstation.stationapi.api.entity.player.CanPlayerRemoveMetaBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RemoteClientInteractionManager.class)
public class MixinRemoteClientInteractionManager {

    private int capturedMeta;

    @Redirect(method = {"method_1707(IIII)V", "method_1721(IIII)V"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        return ((PlayerBlockHardnessPerMeta) blockBase).getHardness(arg, arg.level.getTileMeta(i, j, k));
    }

    @Inject(method = "method_1716(IIII)Z", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/level/Level;getTileMeta(III)I", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureMeta(int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir, int var5, int var6) {
        capturedMeta = var6;
    }

    @Redirect(method = "method_1716(IIII)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/AbstractClientPlayer;canRemoveBlock(Lnet/minecraft/block/BlockBase;)Z"))
    private boolean canRemoveBlock(AbstractClientPlayer abstractClientPlayer, BlockBase arg) {
        return ((CanPlayerRemoveMetaBlock) abstractClientPlayer).canRemoveBlock(arg, capturedMeta);
    }
}
