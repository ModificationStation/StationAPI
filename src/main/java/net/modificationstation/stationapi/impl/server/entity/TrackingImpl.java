package net.modificationstation.stationapi.impl.server.entity;

import net.minecraft.entity.EntityBase;
import net.minecraft.network.EntityHashSet;
import net.minecraft.server.network.ServerEntityTracker;
import net.modificationstation.stationapi.api.common.event.EventListener;
import net.modificationstation.stationapi.api.common.event.ListenerPriority;
import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.common.util.TriState;
import net.modificationstation.stationapi.api.server.entity.ICustomTracking;
import net.modificationstation.stationapi.api.server.entity.ITracking;
import net.modificationstation.stationapi.api.server.entity.Tracking;
import net.modificationstation.stationapi.api.server.event.network.TrackEntityEvent;

/**
 * {@link ITracking} implementation class.
 * @author mine_diver
 * @see TrackEntityEvent
 * @see ICustomTracking
 * @see ITracking
 * @see Tracking
 */
@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class TrackingImpl {

    /**
     * Handles entity's {@link Tracking} annotation if it's present via {@link TrackEntityEvent} hook.
     * @param event the {@link TrackEntityEvent} event.
     * @see TrackEntityEvent
     */
    @EventListener(priority = ListenerPriority.HIGH)
    private static void trackEntity(TrackEntityEvent event) {
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
    public static void track(ServerEntityTracker entityTracker, EntityHashSet trackedEntities, EntityBase entityToTrack, int trackingDistance, int updatePeriod, TriState sendVelocity) {
        if (trackedEntities.containsId(entityToTrack.entityId))
            entityTracker.method_1669(entityToTrack);
        if (sendVelocity == TriState.UNSET)
            entityTracker.method_1666(entityToTrack, trackingDistance, updatePeriod);
        else
            entityTracker.trackEntity(entityToTrack, trackingDistance, updatePeriod, sendVelocity.getBool());
    }
}
