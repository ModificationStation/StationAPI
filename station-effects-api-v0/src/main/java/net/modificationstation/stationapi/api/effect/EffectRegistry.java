package net.modificationstation.stationapi.api.effect;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.util.Identifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class EffectRegistry {
	private static final Reference2ReferenceMap<Identifier, Constructor<? extends EntityEffect<? extends Entity>>> EFFECTS = new Reference2ReferenceOpenHashMap<>();
	private static final Reference2ReferenceMap<Class<? extends EntityEffect<? extends Entity>>, Identifier> EFFECTS_IDS = new Reference2ReferenceOpenHashMap<>();
	
	public static void register(Identifier id, Class<? extends EntityEffect<? extends Entity>> effectClass) {
		Constructor<? extends EntityEffect<? extends Entity>> constructor;
		//noinspection unchecked
		constructor = (Constructor<? extends EntityEffect<? extends Entity>>) effectClass.getConstructors()[0];
		EFFECTS.put(id, constructor);
		EFFECTS_IDS.put(effectClass, id);
	}
	
	public static EntityEffect<? extends Entity> makeEffect(Entity entity, Identifier id) {
		Constructor<? extends EntityEffect<? extends Entity>> constructor = EFFECTS.get(id);
		EntityEffect<? extends Entity> effect = null;
		if (constructor == null) {
			assert StationAPI.LOGGER != null;
			StationAPI.LOGGER.warn("Effect " + id + " is not registered");
		}
		else {
			try {
				effect = constructor.newInstance(id, entity);
			}
			catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return effect;
	}
	
	public static Identifier getID(EntityEffect<? extends Entity> effect) {
		return EFFECTS_IDS.get(effect.getClass());
	}
}
