package net.modificationstation.stationapi.api.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.effect.EntityEffect;
import net.modificationstation.stationapi.api.effect.EntityEffectType;
import net.modificationstation.stationapi.api.util.Util;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

public interface StationEffectsEntity {
    /**
     * Adds specified effect to the entity.
     *
     * @param effectType {@link EntityEffectType} effect ID.
     * @param ticks      ticks for effect to be applied.
     */
    default void addEffect(EntityEffectType<?> effectType, int ticks) {
        Util.assertImpl();
    }

    /**
     * Adds specified effect to the entity, effect will be infinite - it will be applied until manually removed.
     *
     * @param effectType {@link EntityEffectType} effect ID.
     */
    default void addInfiniteEffect(EntityEffectType<?> effectType) {
        addEffect(effectType, EntityEffect.INFINITY_TICKS);
    }

    default <EFFECT_INSTANCE extends EntityEffect<EFFECT_INSTANCE>> EFFECT_INSTANCE getEffect(
            EntityEffectType<EFFECT_INSTANCE> effectType
    ) {
        return Util.assertImpl();
    }

    /**
     * Get all entity effects.
     *
     * @return {@link Collection} of effect {@link EntityEffectType} ID
     */
    default Collection<EntityEffect<?>> getEffects() {
        return Util.assertImpl();
    }

    /**
     * Check if entity has specified effect.
     *
     * @param effectType {@link EntityEffectType} effect ID.
     */
    default boolean hasEffect(EntityEffectType<?> effectType) {
        return Util.assertImpl();
    }
    
    /**
     * Removes specified effect from entity if the effect exists.
     *
     * @param effectType {@link EntityEffectType} effect ID.
     */
    default void removeEffect(EntityEffectType<?> effectType) {
        Util.assertImpl();
    }
    
    default void removeAllEffects() {
        Util.assertImpl();
    }

    @Environment(EnvType.CLIENT)
    @Deprecated
    @ApiStatus.Internal
    default void addEffect(EntityEffect<?> effect) {
        Util.assertImpl();
    }
}
