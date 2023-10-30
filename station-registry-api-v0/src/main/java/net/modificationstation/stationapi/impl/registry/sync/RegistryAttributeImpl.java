package net.modificationstation.stationapi.impl.registry.sync;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.modificationstation.stationapi.api.event.registry.RegistryAttribute;
import net.modificationstation.stationapi.api.event.registry.RegistryAttributeHolder;
import net.modificationstation.stationapi.api.registry.RegistryKey;

import java.util.EnumSet;

public final class RegistryAttributeImpl implements RegistryAttributeHolder {
	private static final Reference2ReferenceMap<RegistryKey<?>, RegistryAttributeHolder> HOLDER_MAP = new Reference2ReferenceOpenHashMap<>();

	public static RegistryAttributeHolder getHolder(RegistryKey<?> registryKey) {
		return HOLDER_MAP.computeIfAbsent(registryKey, key -> new RegistryAttributeImpl());
	}

	private final EnumSet<RegistryAttribute> attributes = EnumSet.noneOf(RegistryAttribute.class);

	private RegistryAttributeImpl() {}

	@Override
	public RegistryAttributeHolder addAttribute(RegistryAttribute attribute) {
		attributes.add(attribute);
		return this;
	}

	@Override
	public boolean hasAttribute(RegistryAttribute attribute) {
		return attributes.contains(attribute);
	}
}