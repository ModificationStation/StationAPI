package net.modificationstation.stationapi.mixin.registry.client;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.RegistriesFrozenEvent;
import net.modificationstation.stationapi.api.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
class MinecraftMixin {
    @Inject(
            method = "run",
            at = @At("HEAD"),
            remap = false
    )
    private void stationapi_freeze(CallbackInfo ci) {
        Registries.bootstrap();
        StationAPI.EVENT_BUS.post(new RegistriesFrozenEvent());
    }
}
