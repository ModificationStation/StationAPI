package net.modificationstation.stationapi.mixin.lifecycle.server;

import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.init.InitFinishedEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftServer.class, priority = 800)
public class MinecraftServerInitMixin {
    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_endInit(CallbackInfoReturnable<Boolean> cir) {
        StationAPI.EVENT_BUS.post(new InitFinishedEvent());
    }
}
