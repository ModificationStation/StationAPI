package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.init.InitFinishedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 800)
public class MinecraftInitMixin {
    @Inject(
            method = "run",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;init()V", shift = At.Shift.AFTER)
    )
    private void stationapi_endInit(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new InitFinishedEvent());
    }
}
