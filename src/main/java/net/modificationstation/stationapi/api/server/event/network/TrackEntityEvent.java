package net.modificationstation.stationapi.api.server.event.network;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.EntityBase;
import net.minecraft.network.EntityHashSet;
import net.minecraft.server.network.ServerEntityTracker;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.util.API;
import net.modificationstation.stationapi.api.server.entity.ICustomTracking;
import net.modificationstation.stationapi.api.server.entity.ITracking;
import net.modificationstation.stationapi.api.server.entity.Tracking;

/**
 * Event that gets called after server tries tracking an entity by checking if it's instance of a vanilla class.
 * @author mine_diver
 * @see ICustomTracking
 * @see ITracking
 * @see Tracking
 */
@RequiredArgsConstructor
public class TrackEntityEvent extends Event {

    /**
     * The dimension's tracker instance. Can be used to (un)track entities.
     */
    public final ServerEntityTracker entityTracker;

    /**
     * The set of tracked entities. Can be used to check if entity is already tracked.
     */
    public final EntityHashSet trackedEntities;

    /**
     * The entity that server tries to track.
     */
    public final EntityBase entityToTrack;

    /**
     * Shortcut for checking if entity is already tracked.
     * @return true if entity is tracked, false otherwise.
     */
    @API
    public boolean isTracked() {
        return trackedEntities.containsId(entityToTrack.entityId);
    }

    /**
     * Shortcut for untracking the entity.
     */
    @API
    public void untrack() {
        entityTracker.method_1669(entityToTrack);
    }

    /**
     * Shortcut for tracking the entity with default sendVelocity value (can be tampered with by other mods using Mixins).
     * @param trackingDistance the distance from the player to the entity in blocks within which the entity should be sent to client (tracked).
     * @param updatePeriod the period in ticks with which the entity updates should be sent to client (position, velocity, etc).
     * @see TrackEntityEvent#track(int, int, boolean)
     */
    @API
    public void track(int trackingDistance, int updatePeriod) {
        entityTracker.method_1666(entityToTrack, trackingDistance, updatePeriod);
    }

    /**
     * Shortcut for tracking the entity.
     * @param trackingDistance the distance from a player to the entity in blocks within which the entity should be sent to clients (tracked).
     * @param updatePeriod the period in ticks with which the entity updates should be sent to clients (position, velocity, etc).
     * @param sendVelocity whether or not should server send velocity updates to clients (fireballs don't send velocity, because client can calculate it itself, and paintings don't have velocity at all).
     * @see TrackEntityEvent#track(int, int)
     */
    @API
    public void track(int trackingDistance, int updatePeriod, boolean sendVelocity) {
        entityTracker.trackEntity(entityToTrack, trackingDistance, updatePeriod, sendVelocity);
    }
}
