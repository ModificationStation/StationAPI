package net.modificationstation.stationapi.mixin.keybinding.client;

import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
class ScreenMixin {
    @Inject(
            method = "tickInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;onKeyboardEvent()V",
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_keyStateChange(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(KeyStateChangedEvent.builder().environment(KeyStateChangedEvent.Environment.IN_GUI).build());
    }
}
