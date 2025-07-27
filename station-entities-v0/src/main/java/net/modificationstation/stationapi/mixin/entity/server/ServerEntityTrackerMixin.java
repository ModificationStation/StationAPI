package net.modificationstation.stationapi.mixin.entity.server;

import net.minecraft.entity.Entity;
import net.minecraft.server.entity.EntityTracker;
import net.minecraft.util.IntHashMap;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTracker.class)
class ServerEntityTrackerMixin {
    @Shadow private IntHashMap field_2005;

    @Inject(
            method = "method_1665",
            at = @At("RETURN")
    )
    private void stationapi_afterVanillaEntries(Entity arg, CallbackInfo ci) {
        //noinspection DataFlowIssue
        StationAPI.EVENT_BUS.post(
                TrackEntityEvent.builder()
                        .entityTracker((EntityTracker) (Object) this)
                        .trackedEntities(field_2005)
                        .entityToTrack(arg)
                        .build()
        );
    }
}
