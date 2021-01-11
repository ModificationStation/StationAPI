package net.modificationstation.stationapi.api.server.entity;

import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.server.event.network.TrackEntity;

/**
 * Entity interface that's used to do custom entity tracking logic inside the entity class via {@link TrackEntity} hook.
 * Only use it if {@link Tracking} and {@link Tracking.At} aren't enough.
 * @author mine_diver
 * @see TrackEntity
 * @see Tracking
 * @see Tracking.At
 */
public interface CustomTracking {

    /**
     * Track method that gets called after server tries tracking an entity by checking if it's instance of a vanilla class.
     * @param entityTracker current dimension's tracker instance. Can be used to (un)track entities.
     * @param trackedEntities the set of tracked entities. Can be used to check if entity is already tracked.
     */
    void track(class_488 entityTracker, class_80 trackedEntities);

    /**
     * Invokes {@link CustomTracking#track(class_488, class_80)} in entity if it's instance of {@link CustomTracking}.
     * @param entityTracker the dimension's tracker instance.
     * @param trackedEntities the set of tracked entities.
     * @param entityToTrack the entity to invoke the method on.
     */
    static void invoke(class_488 entityTracker, class_80 trackedEntities, EntityBase entityToTrack) {
        if (entityToTrack instanceof CustomTracking)
            ((CustomTracking) entityToTrack).track(entityTracker, trackedEntities);
    }
}
