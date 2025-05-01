package net.modificationstation.stationapi.impl.server.entity;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.class_488;
import net.minecraft.class_80;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.server.entity.CustomTracking;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.server.entity.TrackingParametersProvider;
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;

import java.lang.invoke.MethodHandles;

/**
 * {@link CustomTracking} implementation class.
 * @author mine_diver
 * @see TrackEntityEvent
 * @see CustomTracking
 * @see TrackingParametersProvider
 * @see HasTrackingParameters
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class CustomTrackingImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    /**
     * Invokes {@link CustomTracking#track(class_488, class_80)} in entity if it's instance of {@link CustomTracking} via {@link TrackEntityEvent} hook.
     * @param event the {@link TrackEntityEvent} event.
     * @see TrackEntityEvent
     */
    @EventListener
    private static void trackEntity(TrackEntityEvent event) {
        if (event.entityToTrack instanceof CustomTracking trackable)
            trackable.track(event.entityTracker, event.trackedEntities);
    }
}
