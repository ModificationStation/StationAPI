package net.modificationstation.stationapi.impl.client.resource;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.ResourceType;

import java.util.Set;

public interface SuperResourcePack {

    boolean stationapi_superContains(ResourceType type, Identifier id);

    Set<String> stationapi_getSuperNamespaces();
}
