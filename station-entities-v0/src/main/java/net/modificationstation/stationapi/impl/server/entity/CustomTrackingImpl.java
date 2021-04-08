package net.modificationstation.stationapi.impl.server.entity;

import net.minecraft.network.EntityHashSet;
import net.minecraft.server.network.ServerEntityTracker;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.server.entity.ICustomTracking;
import net.modificationstation.stationapi.api.server.entity.ITracking;
import net.modificationstation.stationapi.api.server.entity.Tracking;
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;

/**
 * {@link ICustomTracking} implementation class.
 * @author mine_diver
 * @see TrackEntityEvent
 * @see ICustomTracking
 * @see ITracking
 * @see Tracking
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public final class CustomTrackingImpl {

    /**
     * Invokes {@link ICustomTracking#track(ServerEntityTracker, EntityHashSet)} in entity if it's instance of {@link ICustomTracking} via {@link TrackEntityEvent} hook.
     * @param event the {@link TrackEntityEvent} event.
     * @see TrackEntityEvent
     */
    @EventListener(priority = ListenerPriority.HIGH)
    private static void trackEntity(TrackEntityEvent event) {
        if (event.entityToTrack instanceof ICustomTracking)
            ((ICustomTracking) event.entityToTrack).track(event.entityTracker, event.trackedEntities);
    }
}
