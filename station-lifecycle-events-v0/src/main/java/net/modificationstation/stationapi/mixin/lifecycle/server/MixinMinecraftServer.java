package net.modificationstation.stationapi.mixin.lifecycle.server;

import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.tick.GameTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(
            method = "skipNight()V",
            at = @At("RETURN")
    )
    private void endTick(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(GameTickEvent.End.builder().build());
    }
}
