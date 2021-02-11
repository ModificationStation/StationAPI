package net.modificationstation.stationapi.impl.server.entity;

import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.EntityBase;
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
public class CustomTrackingImpl implements TrackEntity {

    /**
     * Invokes {@link ICustomTracking#track(class_488, class_80)} in entity if it's instance of {@link ICustomTracking} via {@link TrackEntity} hook.
     * @param entityTracker the dimension's tracker instance. Can be used to (un)track entities.
     * @param trackedEntities the set of tracked entities. Can be used to check if entity is already tracked.
     * @param entityToTrack the entity that server tries to track.
     */
    @Override
    public void trackEntity(class_488 entityTracker, class_80 trackedEntities, EntityBase entityToTrack) {
        if (entityToTrack instanceof ICustomTracking)
            ((ICustomTracking) entityToTrack).track(entityTracker, trackedEntities);
    }
}
