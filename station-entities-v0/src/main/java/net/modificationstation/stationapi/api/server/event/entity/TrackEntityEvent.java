package net.modificationstation.stationapi.api.server.event.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.entity.Entity;
import net.minecraft.server.entity.EntityTracker;
import net.minecraft.util.IntHashMap;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.server.entity.CustomTracking;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.server.entity.TrackingParametersProvider;
import net.modificationstation.stationapi.api.util.API;

/**
 * Event that gets called after server tries tracking an entity by checking if it's instance of a vanilla class.
 * @author mine_diver
 * @see CustomTracking
 * @see TrackingParametersProvider
 * @see HasTrackingParameters
 */
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class TrackEntityEvent extends Event {
    /**
     * The dimension's tracker instance. Can be used to (un)track entities.
     */
    public final EntityTracker entityTracker;

    /**
     * The set of tracked entities. Can be used to check if entity is already tracked.
     */
    public final IntHashMap trackedEntities;

    /**
     * The entity that server tries to track.
     */
    public final Entity entityToTrack;

    /**
     * Shortcut for checking if entity is already tracked.
     * @return true if entity is tracked, false otherwise.
     */
    @API
    public boolean isTracked() {
        return trackedEntities.containsKey(entityToTrack.id);
    }

    /**
     * Shortcut for untracking the entity.
     */
    @API
    public void untrack() {
        entityTracker.onEntityRemoved(entityToTrack);
    }

    /**
     * Shortcut for tracking the entity with default sendVelocity value (can be tampered with by other mods using Mixins).
     * @param trackingDistance the distance from the player to the entity in blocks within which the entity should be sent to client (tracked).
     * @param updatePeriod the period in ticks with which the entity updates should be sent to client (position, velocity, etc).
     * @see TrackEntityEvent#track(int, int, boolean)
     */
    @API
    public void track(int trackingDistance, int updatePeriod) {
        entityTracker.startTracking(entityToTrack, trackingDistance, updatePeriod);
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
        entityTracker.startTracking(entityToTrack, trackingDistance, updatePeriod, sendVelocity);
    }
}
