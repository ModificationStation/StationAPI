package net.modificationstation.stationapi.mixin.registry.server;

import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistriesFrozenEvent;
import net.modificationstation.stationapi.api.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(
            method = "method_2166",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
            )
    )
    private void stationapi_freeze(CallbackInfoReturnable<Boolean> cir) {
        Registries.bootstrap();
        StationAPI.EVENT_BUS.post(new RegistriesFrozenEvent());
    }
}
