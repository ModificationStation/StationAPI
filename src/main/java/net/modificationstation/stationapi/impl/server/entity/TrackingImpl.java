package net.modificationstation.stationapi.impl.server.entity;

import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.common.util.TriState;
import net.modificationstation.stationapi.api.server.entity.ICustomTracking;
import net.modificationstation.stationapi.api.server.entity.ITracking;
import net.modificationstation.stationapi.api.server.entity.Tracking;
import net.modificationstation.stationapi.api.server.event.network.TrackEntity;

/**
 * {@link ITracking} implementation class.
 * @author mine_diver
 * @see TrackEntity
 * @see ICustomTracking
 * @see ITracking
 * @see Tracking
 */
public class TrackingImpl {

    /**
     * Handles entity's {@link Tracking} annotation if it's present via {@link TrackEntity} hook.
     * @see TrackEntity
     */
    public static void trackEntity(TrackEntity event) {
        Class<? extends EntityBase> entityClass = event.entityToTrack.getClass();
        if (entityClass.isAnnotationPresent(Tracking.class)) {
            Tracking at = entityClass.getAnnotation(Tracking.class);
            track(event.entityTracker, event.trackedEntities, event.entityToTrack, at.trackingDistance(), at.updatePeriod(), at.sendVelocity());
        }
    }

    /**
     * Tracking logic implementation.
     * @param entityTracker the dimension's tracker instance. Can be used to (un)track entities.
     * @param trackedEntities the set of tracked entities. Can be used to check if entity is already tracked.
     * @param entityToTrack the entity that server tries to track.
     * @param trackingDistance the distance from the player to the entity in blocks within which the entity should be sent to client (tracked).
     * @param updatePeriod the period in ticks with which the entity updates should be sent to client (position, velocity, etc).
     * @param sendVelocity whether or not should server send velocity updates to clients (fireballs don't send velocity, because client can calculate it itself, and paintings don't have velocity at all).
     */
    public static void track(class_488 entityTracker, class_80 trackedEntities, EntityBase entityToTrack, int trackingDistance, int updatePeriod, TriState sendVelocity) {
        if (trackedEntities.method_777(entityToTrack.entityId))
            entityTracker.method_1669(entityToTrack);
        if (sendVelocity == TriState.UNSET)
            entityTracker.method_1666(entityToTrack, trackingDistance, updatePeriod);
        else
            entityTracker.method_1667(entityToTrack, trackingDistance, updatePeriod, sendVelocity.getBool());
    }
}
