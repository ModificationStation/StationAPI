package net.modificationstation.stationapi.api.server.entity;

import net.minecraft.entity.EntityBase;
import net.minecraft.network.EntityHashSet;
import net.minecraft.server.network.ServerEntityTracker;
import net.modificationstation.stationapi.api.server.event.entity.TrackEntityEvent;
import net.modificationstation.stationapi.api.util.TriState;
import net.modificationstation.stationapi.impl.server.entity.TrackingParametersImpl;

/**
 * An implementation of {@link CustomTracking} that has the logic set up and just requires entity to provide data for tracking.
 * If the data is constant, use {@link HasTrackingParameters}.
 * @author mine_diver
 * @see TrackEntityEvent
 * @see CustomTracking
 * @see HasTrackingParameters
 */
public interface TrackingParametersProvider extends CustomTracking {

    /**
     * The logic implementation.
     * @param entityTracker current dimension's tracker instance. Can be used to (un)track entities.
     * @param trackedEntities the set of tracked entities. Can be used to check if entity is already tracked.
     */
    @Override
    default void track(ServerEntityTracker entityTracker, EntityHashSet trackedEntities) {
        TrackingParametersImpl.track(entityTracker, trackedEntities, (EntityBase) this, getTrackingDistance(), getUpdatePeriod(), sendVelocity());
    }

    /**
     * TrackingDistance supplier method.
     * @return the distance from the player to the entity in blocks within which the entity should be sent to client (tracked).
     */
    int getTrackingDistance();

    /**
     * UpdatePeriod supplier method.
     * @return the period in ticks with which the entity updates should be sent to client (position, velocity, etc).
     */
    int getUpdatePeriod();

    /**
     * SendVelocity supplier method.
     * @return whether or not should server send velocity updates to clients (fireballs don't send velocity, because client can calculate it itself, and paintings don't have velocity at all).
     */
    default TriState sendVelocity() {
        return TriState.UNSET;
    }
}
