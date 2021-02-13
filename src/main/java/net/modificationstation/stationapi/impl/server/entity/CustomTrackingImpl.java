package net.modificationstation.stationapi.impl.server.entity;

import net.minecraft.network.EntityHashSet;
import net.minecraft.server.network.ServerEntityTracker;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.server.entity.ICustomTracking;
import net.modificationstation.stationapi.api.server.entity.ITracking;
import net.modificationstation.stationapi.api.server.entity.Tracking;
import net.modificationstation.stationapi.api.server.event.network.TrackEntity;

/**
 * {@link ICustomTracking} implementation class.
 * @author mine_diver
 * @see TrackEntity
 * @see ICustomTracking
 * @see ITracking
 * @see Tracking
 */
public class CustomTrackingImpl {

    /**
     * Invokes {@link ICustomTracking#track(ServerEntityTracker, EntityHashSet)} in entity if it's instance of {@link ICustomTracking} via {@link TrackEntity} hook.
     * @see TrackEntity
     */
    @EventListener
    public static void trackEntity(TrackEntity event) {
        if (event.entityToTrack instanceof ICustomTracking)
            ((ICustomTracking) event.entityToTrack).track(event.entityTracker, event.trackedEntities);
    }
}
