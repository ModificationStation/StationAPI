package net.modificationstation.stationapi.api.effect;

import it.unimi.dsi.fastutil.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Collection;

public interface StationEffectEntity {
	/**
	 * Adds specified effect to the entity, effect will last for infinity - it will be applied until manually removed.
	 * @param effectID {@link Identifier} effect ID.
	 */
	default void addInfinityEffect(Identifier effectID) {
		addEffect(effectID, EntityEffect.INFINITY_TICKS);
	}
	
	/**
	 * Adds specified effect to the entity.
	 * @param effectID {@link Identifier} effect ID.
	 * @param ticks ticks for effect to be applied.
	 */
	default void addEffect(Identifier effectID, int ticks) {
		Util.assertImpl();
	}
	
	/**
	 * Removes specified effect from entity if the effect exists.
	 * @param effectID {@link Identifier} effect ID.
	 */
	default void removeEffect(Identifier effectID) {
		Util.assertImpl();
	}
	
	/**
	 * Get all entity effects.
	 * @return {@link Collection} of effect {@link Identifier} ID
	 */
	default Collection<Identifier> getEffects() {
		return Util.assertImpl();
	}
	
	default void removeAllEffects() {
		Util.assertImpl();
	}
	
	/**
	 * Check if entity has specified effect.
	 * @param effectID {@link Identifier} effect ID.
	 */
	default boolean hasEffect(Identifier effectID) {
		return Util.assertImpl();
	}
	
	@Environment(EnvType.CLIENT)
	default Collection<EntityEffect<? extends Entity>> getRenderEffects() {
		return Util.assertImpl();
	}
	
	@Environment(EnvType.CLIENT)
	default void addEffect(EntityEffect<? extends Entity> effect) {
		Util.assertImpl();
	}
	
	@Environment(EnvType.SERVER)
	default Collection<Pair<Identifier, Integer>> getServerEffects() {
		return Util.assertImpl();
	}
}
