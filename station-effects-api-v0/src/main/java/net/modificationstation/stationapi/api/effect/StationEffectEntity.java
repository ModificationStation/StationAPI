package net.modificationstation.stationapi.api.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Collection;

public interface StationEffectEntity {
	default void addEffect(Identifier effectID) {
		Util.assertImpl();
	}
	
	default void removeEffect(Identifier effectID) {
		Util.assertImpl();
	}
	
	default Collection<Identifier> getEffects() {
		return Util.assertImpl();
	}
	
	default void removeAllEffects() {
		Util.assertImpl();
	}
	
	default boolean hasEffect(Identifier effectID) {
		return Util.assertImpl();
	}
	
	@Environment(EnvType.CLIENT)
	default Collection<EntityEffect<? extends Entity>> getRenderEffects() {
		return Util.assertImpl();
	}
}
