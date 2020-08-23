package net.modificationstation.stationloader.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationloader.api.client.event.keyboard.KeyPressed;
import net.modificationstation.stationloader.api.client.event.texture.TextureRegister;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@Environment(EnvType.CLIENT)
public class MixinMinecraft {

    @Inject(method = "init()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;textureManager:Lnet/minecraft/client/texture/TextureManager;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void textureManagerInit(CallbackInfo ci) {
        TextureRegister.EVENT.getInvoker().registerTextures();
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;hasLevel()Z", shift = At.Shift.BEFORE))
    private void onKeyboardEvent(CallbackInfo ci) {
        KeyPressed.EVENT.getInvoker().keyPressed();
    }
}
