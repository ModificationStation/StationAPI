package net.modificationstation.stationapi.api.event.registry;

import net.fabricmc.fabric.api.event.Event;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ListenableRegistry;

@FunctionalInterface
public interface RegistryEntryRemovedCallback<T> {
	void onEntryRemoved(int rawId, Identifier id, T object);

	static <T> Event<RegistryEntryRemovedCallback<T>> event(ListenableRegistry<T> registry) {
		return registry.getRemoveObjectEvent();
	}
}
