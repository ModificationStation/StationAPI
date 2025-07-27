package net.modificationstation.stationapi.mixin.resourceloader.server;

import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.resource.DataReloadEvent;
import net.modificationstation.stationapi.api.event.resource.DataResourceReloaderRegisterEvent;
import net.modificationstation.stationapi.api.resource.DataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
class MinecraftServerMixin {
    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/logging/Logger;info(Ljava/lang/String;)V",
                    ordinal = 3
            )
    )
    private void stationapi_loadData(CallbackInfoReturnable<Boolean> cir) {
        StationAPI.EVENT_BUS.post(
                DataResourceReloaderRegisterEvent.builder()
                        .resourceManager(DataManager.INSTANCE)
                        .build()
        );
        StationAPI.EVENT_BUS.post(DataReloadEvent.builder().build());
    }
}
