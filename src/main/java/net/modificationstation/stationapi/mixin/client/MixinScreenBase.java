package net.modificationstation.stationapi.mixin.client;

import net.minecraft.client.gui.screen.ScreenBase;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChanged;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenBase.class)
public class MixinScreenBase {

    @Inject(method = "method_130()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ScreenBase;onKeyboardEvent()V", shift = At.Shift.AFTER))
    private void keyStateChange(CallbackInfo ci) {
        if (Keyboard.getEventKeyState())
            KeyStateChanged.EVENT.getInvoker().keyStateChange(KeyStateChanged.Environment.IN_GUI);
        else
            KeyStateChanged.EVENT.getInvoker().keyStateChange(KeyStateChanged.Environment.IN_GUI);
    }
}
