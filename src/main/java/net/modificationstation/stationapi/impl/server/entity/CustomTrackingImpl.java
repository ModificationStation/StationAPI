package net.modificationstation.stationapi.impl.server.entity;

import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.server.entity.CustomTracking;
import net.modificationstation.stationapi.api.server.entity.Tracking;
import net.modificationstation.stationapi.api.server.event.network.TrackEntity;

/**
 * {@link CustomTracking} implementation class.
 * @author mine_diver
 * @see TrackEntity
 * @see CustomTracking
 * @see Tracking
 * @see Tracking.At
 */
public class CustomTrackingImpl implements TrackEntity {

    /**
     * Invokes {@link CustomTracking#track(class_488, class_80)} in entity if it's instance of {@link CustomTracking} via {@link TrackEntity} hook.
     * @param entityTracker the dimension's tracker instance. Can be used to (un)track entities.
     * @param trackedEntities the set of tracked entities. Can be used to check if entity is already tracked.
     * @param entityToTrack the entity that server tries to track.
     */
    @Override
    public void trackEntity(class_488 entityTracker, class_80 trackedEntities, EntityBase entityToTrack) {
        if (entityToTrack instanceof CustomTracking)
            ((CustomTracking) entityToTrack).track(entityTracker, trackedEntities);
    }
}
