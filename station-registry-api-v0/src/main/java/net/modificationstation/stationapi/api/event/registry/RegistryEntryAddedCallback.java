package net.modificationstation.stationapi.api.event.registry;

import net.fabricmc.fabric.api.event.Event;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;

@FunctionalInterface
public interface RegistryEntryAddedCallback<T> {
	void onEntryAdded(int rawId, Identifier id, T object);

	static <T> Event<RegistryEntryAddedCallback<T>> event(ListenableRegistry<T> registry) {
		return registry.getAddObjectEvent();
	}
}
