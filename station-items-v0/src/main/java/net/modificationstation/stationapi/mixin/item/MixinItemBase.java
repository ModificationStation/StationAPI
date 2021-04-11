package net.modificationstation.stationapi.mixin.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.ItemEvent;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBase.class)
public class MixinItemBase {

    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/stat/Stats;setupItemStats()V", shift = At.Shift.BEFORE))
    private static void afterItemRegister(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new ItemRegistryEvent());
    }

    @ModifyVariable(method = "setTranslationKey(Ljava/lang/String;)Lnet/minecraft/item/ItemBase;", at = @At("HEAD"))
    private String getName(String name) {
        return StationAPI.EVENT_BUS.post(new ItemEvent.TranslationKeyChanged((ItemBase) (Object) this, name)).currentTranslationKey;
    }
}
