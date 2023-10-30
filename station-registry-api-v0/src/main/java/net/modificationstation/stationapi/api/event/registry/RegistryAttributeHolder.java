package net.modificationstation.stationapi.api.event.registry;

import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.impl.registry.sync.RegistryAttributeImpl;

public interface RegistryAttributeHolder {
	static RegistryAttributeHolder get(RegistryKey<?> registryKey) {
		return RegistryAttributeImpl.getHolder(registryKey);
	}

	static RegistryAttributeHolder get(Registry<?> registry) {
		return get(registry.getKey());
	}

	RegistryAttributeHolder addAttribute(RegistryAttribute attribute);

	boolean hasAttribute(RegistryAttribute attribute);
}