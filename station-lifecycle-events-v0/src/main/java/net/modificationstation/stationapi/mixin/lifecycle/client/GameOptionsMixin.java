package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.client.option.GameOptions;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.event.option.GameOptionsEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
class GameOptionsMixin {
    @Inject(
            method = "load",
            at = @At("RETURN")
    )
    private void stationapi_gameOptionsLoad(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(GameOptionsEvent.Load.builder().gameOptions((GameOptions) (Object) this).build());
    }

    @Inject(
            method = "save",
            at = @At("RETURN")
    )
    private void stationapi_gameOptionsSave(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(GameOptionsEvent.Save.builder().gameOptions((GameOptions) (Object) this).build());
    }
}
