package net.modificationstation.stationapi.mixin.keybinding.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    public ScreenBase currentScreen;

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ScreenBase;onKeyboardEvent()V", shift = At.Shift.AFTER))
    private void keyPressInGUI(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new KeyStateChangedEvent(KeyStateChangedEvent.Environment.IN_GUI));
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;hasLevel()Z", shift = At.Shift.BEFORE, ordinal = 0))
    private void keyPressInGame(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new KeyStateChangedEvent(KeyStateChangedEvent.Environment.IN_GAME));
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", ordinal = 1, shift = At.Shift.BEFORE))
    private void keyReleased(CallbackInfo ci) {
        if (!Keyboard.getEventKeyState())
            if (currentScreen == null)
                StationAPI.EVENT_BUS.post(new KeyStateChangedEvent(KeyStateChangedEvent.Environment.IN_GAME));
            else
                StationAPI.EVENT_BUS.post(new KeyStateChangedEvent(KeyStateChangedEvent.Environment.IN_GUI));
    }
}
