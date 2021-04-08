package net.modificationstation.stationapi.mixin.sortme.client;

import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.StationAPI;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@net.fabricmc.api.Environment(EnvType.CLIENT)
public class MixinMinecraft {

    @Shadow public ScreenBase currentScreen;

    @Inject(method = "init()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;textureManager:Lnet/minecraft/client/texture/TextureManager;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void textureManagerInit(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new TextureRegisterEvent());
    }

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
