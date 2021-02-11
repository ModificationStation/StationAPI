package net.modificationstation.stationapi.api.server.event.network;

import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.common.event.GameEventOld;
import net.modificationstation.stationapi.api.common.util.API;
import net.modificationstation.stationapi.api.server.entity.ICustomTracking;
import net.modificationstation.stationapi.api.server.entity.ITracking;
import net.modificationstation.stationapi.api.server.entity.Tracking;

import java.util.function.Consumer;

/**
 * Event that gets called after server tries tracking an entity by checking if it's instance of a vanilla class.
 * @author mine_diver
 * @see ICustomTracking
 * @see ITracking
 * @see Tracking
 */
public interface TrackEntity {

    /**
     * The event instance.
     */
    GameEventOld<TrackEntity> EVENT = new GameEventOld<>(TrackEntity.class,
            listeners ->
                    (entityTracker, trackedEntities, entityToTrack) -> {
                        for (TrackEntity listener : listeners)
                            listener.trackEntity(entityTracker, trackedEntities, entityToTrack);
                    },
            (Consumer<GameEventOld<TrackEntity>>) trackEntity ->
                    trackEntity.register((entityTracker, trackedEntities, entityToTrack) -> GameEventOld.EVENT_BUS.post(new Data(entityTracker, trackedEntities, entityToTrack)))
    );

    /**
     * The event function.
     * @param entityTracker the dimension's tracker instance. Can be used to (un)track entities.
     * @param trackedEntities the set of tracked entities. Can be used to check if entity is already tracked.
     * @param entityToTrack the entity that server tries to track.
     */
    void trackEntity(class_488 entityTracker, class_80 trackedEntities, EntityBase entityToTrack);

    /**
     * The event data used by EventBus.
     */
    final class Data extends GameEventOld.Data<TrackEntity> {

        /**
         * The dimension's tracker instance. Can be used to (un)track entities.
         */
        public final class_488 entityTracker;

        /**
         * The set of tracked entities. Can be used to check if entity is already tracked.
         */
        public final class_80 trackedEntities;

        /**
         * The entity that server tries to track.
         */
        public final EntityBase entityToTrack;

        private Data(class_488 entityTracker, class_80 trackedEntities, EntityBase entityToTrack) {
            super(EVENT);
            this.entityTracker = entityTracker;
            this.trackedEntities = trackedEntities;
            this.entityToTrack = entityToTrack;
        }

        /**
         * Shortcut for checking if entity is already tracked.
         * @return true if entity is tracked, false otherwise.
         */
        @API
        public boolean isTracked() {
            return trackedEntities.method_777(entityToTrack.entityId);
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
         * @see Data#track(int, int, boolean)
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
         * @see Data#track(int, int)
         */
        @API
        public void track(int trackingDistance, int updatePeriod, boolean sendVelocity) {
            entityTracker.method_1667(entityToTrack, trackingDistance, updatePeriod, sendVelocity);
        }
    }
}
