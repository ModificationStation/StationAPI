package net.modificationstation.stationapi.mixin.celestial.server;

import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.celestial.CelestialRegisterEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MinecraftServer.class, priority = 799)
class MinecraftServerMixin {
    @Inject(
            method = "method_2166",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_endInit(CallbackInfoReturnable<Boolean> cir) {
        StationAPI.EVENT_BUS.post(new CelestialRegisterEvent());
    }
}