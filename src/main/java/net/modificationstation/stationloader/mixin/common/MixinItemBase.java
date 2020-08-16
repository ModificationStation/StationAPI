package net.modificationstation.stationloader.mixin.common;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationloader.common.event.item.ItemRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBase.class)
public class MixinItemBase {

    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stats;onItemsRegistered()V", shift = At.Shift.BEFORE))
    private static void afterItemRegister(CallbackInfo ci) {
        ItemRegister.EVENT.getInvoker().registerItems();
    }
}
