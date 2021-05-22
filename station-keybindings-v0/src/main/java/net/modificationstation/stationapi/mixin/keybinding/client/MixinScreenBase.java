package net.modificationstation.stationapi.mixin.keybinding.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenBase.class)
public class MixinScreenBase {

    @Inject(method = "method_130()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ScreenBase;onKeyboardEvent()V", shift = At.Shift.AFTER))
    private void keyStateChange(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new KeyStateChangedEvent(KeyStateChangedEvent.Environment.IN_GUI));
    }
}
