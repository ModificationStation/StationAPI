package net.modificationstation.stationapi.mixin.render.client;

import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
@net.fabricmc.api.Environment(EnvType.CLIENT)
public class MixinMinecraft {

    @Inject(method = "init()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;textureManager:Lnet/minecraft/client/texture/TextureManager;", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void textureManagerInit(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new TextureRegisterEvent());
    }
}
