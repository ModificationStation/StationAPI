package net.modificationstation.stationapi.api.effect;

import it.unimi.dsi.fastutil.objects.ReferenceIntPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Collection;

public interface StationEffectEntity {
    /**
     * Adds specified effect to the entity, effect will be infinite - it will be applied until manually removed.
     *
     * @param effectType {@link EntityEffectType} effect ID.
     */
    default void addInfiniteEffect(EntityEffectType<?> effectType) {
        addEffect(effectType, EntityEffect.INFINITY_TICKS);
    }
    
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
     * Removes specified effect from entity if the effect exists.
     *
     * @param effectType {@link EntityEffectType} effect ID.
     */
    default void removeEffect(EntityEffectType<?> effectType) {
        Util.assertImpl();
    }
    
    /**
     * Get all entity effects.
     *
     * @return {@link Collection} of effect {@link EntityEffectType} ID
     */
    default Collection<EntityEffectType<?>> getEffects() {
        return Util.assertImpl();
    }
    
    default void removeAllEffects() {
        Util.assertImpl();
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
     * Get an amount of remaining ticks that effect will stay on entity. If there is no effect will return 0.
     * If effect is infinity will return {@code EntityEffect.INFINITY_TICKS}
     */
    default int getEffectTicks(EntityEffectType<?> effectType) {
        return Util.assertImpl();
    }
    
    @Environment(EnvType.CLIENT)
    default Collection<EntityEffect<?>> getRenderEffects() {
        return Util.assertImpl();
    }
    
    @Environment(EnvType.CLIENT)
    default void addEffect(EntityEffect<?> effect) {
        Util.assertImpl();
    }
    
    @Environment(EnvType.SERVER)
    default Collection<ReferenceIntPair<EntityEffectType<?>>> getServerEffects() {
        return Util.assertImpl();
    }
}
