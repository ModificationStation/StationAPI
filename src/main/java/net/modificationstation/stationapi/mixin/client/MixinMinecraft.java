package net.modificationstation.stationapi.mixin.client;

import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegister;
import net.modificationstation.stationapi.api.common.StationAPI;
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
        StationAPI.EVENT_BUS.post(new TextureRegister());
    }

//    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ScreenBase;onKeyboardEvent()V", shift = At.Shift.AFTER))
//    private void keyPressInGUI(CallbackInfo ci) {
//        KeyStateChanged.EVENT.getInvoker().keyStateChange(KeyStateChanged.Environment.IN_GUI, KeyStateChanged.State.PRESSED);
//    }
//
//    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;hasLevel()Z", shift = At.Shift.BEFORE, ordinal = 0))
//    private void keyPressInGame(CallbackInfo ci) {
//        KeyStateChanged.EVENT.getInvoker().keyStateChange(KeyStateChanged.Environment.IN_GAME, KeyStateChanged.State.PRESSED);
//    }
//
//    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKeyState()Z", ordinal = 1, shift = At.Shift.BEFORE))
//    private void keyReleased(CallbackInfo ci) {
//        if (!Keyboard.getEventKeyState())
//            if (currentScreen == null)
//                KeyStateChanged.EVENT.getInvoker().keyStateChange(KeyStateChanged.Environment.IN_GAME, KeyStateChanged.State.RELEASED);
//            else
//                KeyStateChanged.EVENT.getInvoker().keyStateChange(KeyStateChanged.Environment.IN_GUI, KeyStateChanged.State.RELEASED);
//    }
}
