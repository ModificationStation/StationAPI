package net.modificationstation.stationapi.api.effect;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class EnitityEffectRegistry extends SimpleRegistry<EntityEffectFactory> {
	public static final RegistryKey<EnitityEffectRegistry> KEY = RegistryKey.ofRegistry(NAMESPACE.id("entity_effects"));
	public static final EnitityEffectRegistry INSTANCE = Registries.create(KEY, new EnitityEffectRegistry(), registry -> null, Lifecycle.experimental());
	
	private EnitityEffectRegistry() {
		super(KEY, Lifecycle.experimental(), false);
	}
}
