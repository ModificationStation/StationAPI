package net.modificationstation.stationapi.api.server.entity;

import net.minecraft.network.EntityHashSet;
import net.minecraft.server.network.ServerEntityTracker;
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;

/**
 * Entity interface that's used to do custom entity tracking logic inside the entity class via {@link TrackEntityEvent} hook.
 * Only use it if {@link ITracking} and {@link Tracking} aren't enough.
 * @author mine_diver
 * @see TrackEntityEvent
 * @see ITracking
 * @see Tracking
 */
public interface ICustomTracking {

    /**
     * Track method that gets called after server tries tracking an entity by checking if it's instance of a vanilla class.
     * @param entityTracker current dimension's tracker instance. Can be used to (un)track entities.
     * @param trackedEntities the set of tracked entities. Can be used to check if entity is already tracked.
     * @see ITracking#track(ServerEntityTracker, EntityHashSet)
     */
    void track(ServerEntityTracker entityTracker, EntityHashSet trackedEntities);
}
