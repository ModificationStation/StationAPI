package net.modificationstation.stationapi.api.server.entity;

import net.minecraft.class_488;
import net.minecraft.class_80;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.common.util.TriState;
import net.modificationstation.stationapi.api.server.event.network.TrackEntity;

import java.lang.annotation.*;

/**
 * An implementation of {@link CustomTracking} that has the logic set up and just requires entity to provide data for tracking.
 * If the data is constant, use {@link Tracking.At}.
 * @author mine_diver
 * @see TrackEntity
 * @see CustomTracking
 * @see Tracking.At
 */
public interface Tracking extends CustomTracking {

    /**
     * The logic implementation.
     * @param entityTracker current dimension's tracker instance. Can be used to (un)track entities.
     * @param trackedEntities the set of tracked entities. Can be used to check if entity is already tracked.
     */
    @Override
    default void track(class_488 entityTracker, class_80 trackedEntities) {
        EntityBase entityToTrack = (EntityBase) this;
        if (trackedEntities.method_777(entityToTrack.entityId))
            entityTracker.method_1669(entityToTrack);
        TriState sendVelocity = sendVelocity();
        if (sendVelocity == TriState.UNSET)
            entityTracker.method_1666(entityToTrack, getTrackingDistance(), getUpdatePeriod());
        else
            entityTracker.method_1667(entityToTrack, getTrackingDistance(), getUpdatePeriod(), sendVelocity.getBool());
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

    /**
     * Annotation implementation of {@link Tracking}.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    @interface At {

        /**
         * TrackingDistance supplier method.
         * @return the distance from the player to the entity in blocks within which the entity should be sent to client (tracked).
         */
        int trackingDistance();

        /**
         * UpdatePeriod supplier method.
         * @return the period in ticks with which the entity updates should be sent to client (position, velocity, etc).
         */
        int updatePeriod();

        /**
         * SendVelocity supplier method.
         * @return whether or not should server send velocity updates to clients (fireballs don't send velocity, because client can calculate it itself, and paintings don't have velocity at all).
         */
        TriState sendVelocity() default TriState.UNSET;
    }

    /**
     * Used to handle entity's {@link At} annotation if it's present via {@link TrackEntity} hook.
     * @param entityTracker the dimension's tracker instance.
     * @param trackedEntities the set of tracked entities.
     * @param entityToTrack the entity to handle the annotation on.
     */
    static void invoke(class_488 entityTracker, class_80 trackedEntities, EntityBase entityToTrack) {
        Class<? extends EntityBase> entityClass = entityToTrack.getClass();
        if (entityClass.isAnnotationPresent(At.class)) {
            At at = entityClass.getAnnotation(At.class);
            TriState sendVelocity = at.sendVelocity();
            if (trackedEntities.method_777(entityToTrack.entityId))
                entityTracker.method_1669(entityToTrack);
            if (sendVelocity == TriState.UNSET)
                entityTracker.method_1666(entityToTrack, at.trackingDistance(), at.updatePeriod());
            else
                entityTracker.method_1667(entityToTrack, at.trackingDistance(), at.updatePeriod(), sendVelocity.getBool());
        }
    }
}
