package net.modificationstation.stationapi.mixin.keybinding.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
class MinecraftMixin {
    @Shadow
    public Screen currentScreen;

    @Inject(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/Screen;onKeyboardEvent()V",
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_keyPressInGUI(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(KeyStateChangedEvent.builder().environment(KeyStateChangedEvent.Environment.IN_GUI).build());
    }

    @Inject(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;isWorldRemote()Z",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            )
    )
    private void stationapi_keyPressInGame(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(KeyStateChangedEvent.builder().environment(KeyStateChangedEvent.Environment.IN_GAME).build());
    }

    @Inject(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z",
                    ordinal = 1,
                    shift = At.Shift.BEFORE,
                    remap = false
            )
    )
    private void stationapi_keyReleased(CallbackInfo ci) {
        if (!Keyboard.getEventKeyState())
            if (currentScreen == null)
                StationAPI.EVENT_BUS.post(KeyStateChangedEvent.builder().environment(KeyStateChangedEvent.Environment.IN_GAME).build());
            else
                StationAPI.EVENT_BUS.post(KeyStateChangedEvent.builder().environment(KeyStateChangedEvent.Environment.IN_GUI).build());
    }
}
