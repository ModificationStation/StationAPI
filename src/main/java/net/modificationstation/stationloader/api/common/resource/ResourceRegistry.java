package net.modificationstation.stationloader.api.common.resource;

import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.Registry;

import java.util.Optional;

public class ResourceRegistry extends Registry<Resource> {

    public ResourceRegistry(Identifier registryId) {
        super(registryId);
    }

    @Override
    public Optional<Resource> getByIdentifier(Identifier identifier) {
        Optional<Resource> resource = super.getByIdentifier(identifier);
        if (!resource.isPresent()) {
            registerValue(identifier, new Resource(identifier, getClass().getResource(String.format("assets/%s/%s", identifier.getModID(), identifier.getId()))));
            resource = super.getByIdentifier(identifier);
        }
        return resource;
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }
}
