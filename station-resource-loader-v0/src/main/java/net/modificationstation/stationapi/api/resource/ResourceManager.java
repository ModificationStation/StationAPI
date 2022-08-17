package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.registry.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Provides resource loading capabilities to Minecraft.
 */
public interface ResourceManager extends ResourceFactory {
    /**
     * Gets a set of all namespaces offered by the resource packs loaded by this manager.
     */
    Set<String> getAllNamespaces();

    /**
     * Gets all of the available resources to the corresponding resource identifier.
     *
     * <p>Resources are returned in load order, or ascending order of priority, so the last element in the returned
     * list is what would be returned normally by {@link #getResource}
     *
     * <p>Each resource in this returned list must be closed to avoid resource leaks.
     *
     * @param id the resource identifier to search for
     */
    List<Resource> getAllResources(Identifier id);

    /**
     * Returns a sorted list of identifiers matching a path predicate.
     *
     * <p>Scanning begins in {@code startingPath} and each candidate file present under that directory
     * will be offered up to the predicate to decide whether it should be included or not.
     *
     * <p>Elements in the returned list may not, necessarily be unique. Additional effort is advised to ensure that
     * duplicates in the returned list are discarded before loading.
     *
     * @return the list matching identifiers
     *
     * @param startingPath the starting path to begin scanning from
     * @param allowedPathPredicate a predicate to determine whether a path should be included or not
     */
    Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate);

    Map<Identifier, List<Resource>> findAllResources(String startingPath, Predicate<Identifier> allowedPathPredicate);

    /**
     * Gets a stream of loaded resource packs in increasing order of priority.
     */
    Stream<ResourcePack> streamResourcePacks();

    enum Empty implements ResourceManager {
        INSTANCE;


        @Override
        public Set<String> getAllNamespaces() {
            return Set.of();
        }

        @Override
        public Optional<Resource> getResource(Identifier identifier) {
            return Optional.empty();
        }

        @Override
        public List<Resource> getAllResources(Identifier id) {
            return List.of();
        }

        @Override
        public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
            return Map.of();
        }

        @Override
        public Map<Identifier, List<Resource>> findAllResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
            return Map.of();
        }

        @Override
        public Stream<ResourcePack> streamResourcePacks() {
            return Stream.of(new ResourcePack[0]);
        }
    }
}

