package net.modificationstation.stationapi.mixin.entity.server;

import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_488.class)
class ServerEntityTrackerMixin {
    @Shadow private class_80 field_2005;

    @Inject(
            method = "method_1665",
            at = @At("RETURN")
    )
    private void stationapi_afterVanillaEntries(Entity arg, CallbackInfo ci) {
        //noinspection DataFlowIssue
        StationAPI.EVENT_BUS.post(
                TrackEntityEvent.builder()
                        .entityTracker((class_488) (Object) this)
                        .trackedEntities(field_2005)
                        .entityToTrack(arg)
                        .build()
        );
    }
}
