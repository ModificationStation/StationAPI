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
public class MixinMinecraft {

    @Inject(
            method = "tick()V",
            at = @At("RETURN")
    )
    private void endTick(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(GameTickEvent.End.builder().build());
    }

    @Inject(
            method = "run()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;printOpenGLError(Ljava/lang/String;)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void startRenderTick(CallbackInfo ci) {
        TickScheduler.CLIENT_RENDER_START.tick();
    }

    @Inject(
            method = "run()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;printOpenGLError(Ljava/lang/String;)V",
                    ordinal = 1
            ),
            remap = false
    )
    private void endRenderTick(CallbackInfo ci) {
        TickScheduler.CLIENT_RENDER_END.tick();
    }
}
