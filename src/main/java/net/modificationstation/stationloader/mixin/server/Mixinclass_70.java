package net.modificationstation.stationloader.mixin.server;

import net.minecraft.block.BlockBase;
import net.minecraft.class_70;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.modificationstation.stationloader.api.common.block.BlockStrengthPerMeta;
import net.modificationstation.stationloader.api.common.item.EffectiveOnMeta;
import net.modificationstation.stationloader.api.common.item.UseFirst;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(class_70.class)
public class Mixinclass_70 {

    @Shadow
    private ServerLevel field_2310;

    @Shadow
    private int field_2318;

    @Shadow
    private int field_2319;

    @Shadow
    private int field_2320;
    private int capturedMeta;

    @Redirect(method = "method_1828()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta(BlockBase blockBase, PlayerBase arg) {
        return ((BlockStrengthPerMeta) blockBase).getBlockStrength(arg, field_2310.getTileMeta(field_2318, field_2319, field_2320));
    }

    @Redirect(method = "method_1830(IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta2(BlockBase blockBase, PlayerBase arg, int i, int j, int k, int i1) {
        return ((BlockStrengthPerMeta) blockBase).getBlockStrength(arg, field_2310.getTileMeta(i, j, k));
    }

    @Redirect(method = "method_1829(III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockBase;getHardness(Lnet/minecraft/entity/player/PlayerBase;)F"))
    private float getHardnessPerMeta3(BlockBase blockBase, PlayerBase arg, int i, int j, int k) {
        return ((BlockStrengthPerMeta) blockBase).getBlockStrength(arg, field_2310.getTileMeta(i, j, k));
    }

    @Inject(method = "method_1834(III)Z", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerLevel;getTileMeta(III)I", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureMeta(int i, int j, int k, CallbackInfoReturnable<Boolean> cir, int var4, int var5) {
        capturedMeta = var5;
    }

    @Redirect(method = "method_1834(III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerBase;canRemoveBlock(Lnet/minecraft/block/BlockBase;)Z"))
    private boolean canRemoveBlock(PlayerBase playerBase, BlockBase arg) {
        if (arg.material.doesRequireTool())
            return true;
        ItemInstance itemInstance = playerBase.getHeldItem();
        return itemInstance != null && ((EffectiveOnMeta) itemInstance.getType()).isEffectiveOn(arg, capturedMeta);
    }

    @Inject(method = "method_1832(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/level/Level;Lnet/minecraft/item/ItemInstance;IIII)Z", at = @At(value = "HEAD"), cancellable = true)
    private void injectOnPlaceBlock(PlayerBase playerBase, Level level, ItemInstance itemInstance, int i, int j, int k, int i1, CallbackInfoReturnable<Boolean> cir) {
        if (itemInstance != null && itemInstance.getType() instanceof UseFirst) {
            if (((UseFirst) itemInstance.getType()).onItemUseFirst(itemInstance, playerBase, level, i, j, k, i1)) {
                cir.setReturnValue(true);
            }
        }
    }
}
