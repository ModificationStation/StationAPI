package net.modificationstation.stationapi.mixin.dimension.client;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(
            method = "init()V",
            at = @At("RETURN")
    )
    private void initDimensions(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(new DimensionRegistryEvent());
    }
}
