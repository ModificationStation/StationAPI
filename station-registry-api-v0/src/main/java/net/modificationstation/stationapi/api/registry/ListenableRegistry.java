package net.modificationstation.stationapi.api.registry;

import net.fabricmc.fabric.api.event.Event;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryAddedCallback;
import net.modificationstation.stationapi.api.event.registry.RegistryEntryRemovedCallback;
import net.modificationstation.stationapi.api.event.registry.RegistryIdRemapCallback;

public interface ListenableRegistry<T> {
	Event<RegistryEntryAddedCallback<T>> getAddObjectEvent();
	Event<RegistryEntryRemovedCallback<T>> getRemoveObjectEvent();
	Event<RegistryIdRemapCallback<T>> getRemapEvent();
}
