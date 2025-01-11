package net.modificationstation.stationapi.api.effect;

import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;

@FunctionalInterface
public interface EntityEffectFactory {
	EntityEffect create(Identifier id, Entity entity, int ticks);
}
