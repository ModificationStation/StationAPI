package net.modificationstation.stationapi.mixin.sortme.server;

import net.minecraft.block.BlockBase;
import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.common.item.EffectiveOnMeta;
import net.modificationstation.stationapi.api.common.item.UseOnBlockFirst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_70.class)
public class Mixinclass_70 {

    private int capturedMeta;

    @Inject(method = "method_1834(III)Z", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerLevel;getTileMeta(III)I", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureMeta(int i, int j, int k, CallbackInfoReturnable<Boolean> cir, int var4, int var5) {
        capturedMeta = var5;
    }

    @Redirect(method = "method_1834(III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerBase;canRemoveBlock(Lnet/minecraft/block/BlockBase;)Z"))
    private boolean canRemoveBlock(PlayerBase playerBase, BlockBase arg) {
        if (arg.material.doesRequireTool())
            return true;
        ItemInstance itemInstance = playerBase.getHeldItem();
        return itemInstance != null && ((EffectiveOnMeta) itemInstance.getType()).isEffectiveOn(arg, capturedMeta, itemInstance);
    }

    @Inject(method = "method_1832(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/level/Level;Lnet/minecraft/item/ItemInstance;IIII)Z", at = @At(value = "HEAD"), cancellable = true)
    private void injectOnPlaceBlock(PlayerBase playerBase, Level level, ItemInstance itemInstance, int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir) {
        if (itemInstance != null && itemInstance.getType() instanceof UseOnBlockFirst) {
            if (((UseOnBlockFirst) itemInstance.getType()).onUseOnBlockFirst(itemInstance, playerBase, level, i, j, k, i1)) {
                cir.setReturnValue(true);
            }
        }
    }
}
