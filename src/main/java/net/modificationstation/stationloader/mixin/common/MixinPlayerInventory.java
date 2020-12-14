package net.modificationstation.stationloader.mixin.common;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.item.HasItemEntity;
import net.modificationstation.stationloader.api.common.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory {

    @Shadow
    public ItemInstance[] main;

    @Inject(method = "method_685(Lnet/minecraft/item/ItemInstance;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemInstance;<init>(III)V", shift = At.Shift.BY, by = 2), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onCopyItemInstance(ItemInstance arg, CallbackInfoReturnable<Integer> cir, int var2, int var3, int var4) {
        ItemEntity itemEntity = HasItemEntity.cast(arg).getItemEntity();
        System.out.println(main[var4]);
        HasItemEntity.cast(main[var4]).setItemEntity(itemEntity == null ? null : itemEntity.copy());
    }
}
