package net.modificationstation.stationapi.mixin.lifecycle.client;

import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.tick.GameTickEvent;
import net.modificationstation.stationapi.api.tick.TickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
class MinecraftMixin {
    @Inject(
            method = "tick",
            at = @At("RETURN")
    )
    private void stationapi_endTick(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(GameTickEvent.End.builder().build());
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Inject(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;logGlError(Ljava/lang/String;)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER,
                    remap = true
            ),
            remap = false
    )
    private void stationapi_startRenderTick(CallbackInfo ci) {
        TickScheduler.CLIENT_RENDER_START.tick();
    }

    @SuppressWarnings("DefaultAnnotationParam")
    @Inject(
            method = "run()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;logGlError(Ljava/lang/String;)V",
                    ordinal = 1,
                    remap = true
            ),
            remap = false
    )
    private void stationapi_endRenderTick(CallbackInfo ci) {
        TickScheduler.CLIENT_RENDER_END.tick();
    }
}
