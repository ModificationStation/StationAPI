package net.modificationstation.stationapi.impl.server.entity;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.network.EntityHashSet;
import net.minecraft.server.network.ServerEntityTracker;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.server.entity.CustomTracking;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.server.entity.TrackingParametersProvider;
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;

/**
 * {@link CustomTracking} implementation class.
 * @author mine_diver
 * @see TrackEntityEvent
 * @see CustomTracking
 * @see TrackingParametersProvider
 * @see HasTrackingParameters
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class CustomTrackingImpl {

    /**
     * Invokes {@link CustomTracking#track(ServerEntityTracker, EntityHashSet)} in entity if it's instance of {@link CustomTracking} via {@link TrackEntityEvent} hook.
     * @param event the {@link TrackEntityEvent} event.
     * @see TrackEntityEvent
     */
    @EventListener(priority = ListenerPriority.HIGH)
    private static void trackEntity(TrackEntityEvent event) {
        if (event.entityToTrack instanceof CustomTracking)
            ((CustomTracking) event.entityToTrack).track(event.entityTracker, event.trackedEntities);
    }
}
