package net.modificationstation.stationapi.impl.resource;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.resource.ResourcePack;
import net.modificationstation.stationapi.api.resource.ResourceType;
import net.modificationstation.stationapi.api.resource.metadata.ResourceFilter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * A basic implementation of resource manager with a lifecycle.
 * 
 * <p>It handles resources by namespaces, hoping that most namespaces are
 * defined in only few resource packs.
 * 
 * @see NamespaceResourceManager
 */
public class LifecycledResourceManagerImpl implements LifecycledResourceManager {
    private final Map<ModID, NamespaceResourceManager> subManagers;
    private final ResourceType resourceType;
    private final List<ResourcePack> packs;

    public LifecycledResourceManagerImpl(ResourceType type, List<ResourcePack> packs) {
        this.packs = List.copyOf(packs);
        resourceType = type;
        Map<ModID, NamespaceResourceManager> map = new HashMap<>();
        List<ModID> list = packs.stream().flatMap(pack -> pack.getNamespaces(type).stream()).distinct().toList();
        for (ResourcePack resourcePack : packs) {
            ResourceFilter resourceFilter = this.parseResourceFilter(resourcePack);
            Set<ModID> set = resourcePack.getNamespaces(type);
            Predicate<Identifier> predicate = resourceFilter != null ? id -> resourceFilter.isPathBlocked(id.id) : null;
            for (ModID string : list) {
                boolean bl = set.contains(string);
                boolean bl2 = resourceFilter != null && resourceFilter.isNamespaceBlocked(string.toString());
                if (!bl && !bl2) continue;
                NamespaceResourceManager namespaceResourceManager = map.get(string);
                if (namespaceResourceManager == null) {
                    namespaceResourceManager = new NamespaceResourceManager(type, string);
                    map.put(string, namespaceResourceManager);
                }
                if (bl && bl2) {
                    namespaceResourceManager.addPack(resourcePack, predicate);
                    continue;
                }
                if (bl) {
                    namespaceResourceManager.addPack(resourcePack);
                    continue;
                }
                namespaceResourceManager.addPack(resourcePack.getName(), predicate);
            }
        }
        this.subManagers = map;
    }

    @Nullable
    private ResourceFilter parseResourceFilter(ResourcePack pack) {
        try {
            return pack.parseMetadata(ResourceFilter.SERIALIZER);
        } catch (IOException iOException) {
            LOGGER.error("Failed to get filter section from pack {}", pack.getName());
            return null;
        }
    }

    @Override
    public Set<ModID> getAllNamespaces() {
        return this.subManagers.keySet();
    }

    @Override
    public Optional<Resource> getResource(Identifier identifier) {
        ResourceManager resourceManager = this.subManagers.get(identifier.modID);
        if (resourceManager != null) {
            return resourceManager.getResource(identifier);
        }
        return Optional.empty();
    }

    @Override
    public List<Resource> getAllResources(Identifier id) {
        ResourceManager resourceManager = this.subManagers.get(id.modID);
        if (resourceManager != null) {
            return resourceManager.getAllResources(id);
        }
        return List.of();
    }

    @Override
    public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        LifecycledResourceManagerImpl.validateStartingPath(startingPath);
        TreeMap<Identifier, Resource> map = new TreeMap<>();
        for (NamespaceResourceManager namespaceResourceManager : this.subManagers.values()) {
            map.putAll(namespaceResourceManager.findResources(startingPath, allowedPathPredicate));
        }
        return map;
    }

    @Override
    public Map<Identifier, List<Resource>> findAllResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        LifecycledResourceManagerImpl.validateStartingPath(startingPath);
        TreeMap<Identifier, List<Resource>> map = new TreeMap<>();
        for (NamespaceResourceManager namespaceResourceManager : this.subManagers.values()) {
            map.putAll(namespaceResourceManager.findAllResources(startingPath, allowedPathPredicate));
        }
        return map;
    }

    /**
     * Validates the starting path to ensure that it does not end with a slash.
     * 
     * @throws IllegalArgumentException if {@code startingPath} has a trailing slash
     */
    private static void validateStartingPath(String startingPath) {
        if (startingPath.endsWith("/")) {
            throw new IllegalArgumentException("Trailing slash in path " + startingPath);
        }
    }

    @Override
    public Stream<ResourcePack> streamResourcePacks() {
        return this.packs.stream();
    }

    @Override
    public ResourceType getResourceType() {
        return resourceType;
    }

    @Override
    public void close() {
        this.packs.forEach(ResourcePack::close);
    }
}

