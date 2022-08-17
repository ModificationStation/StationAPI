package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.Unit;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * A reloadable resource manager is always available to be accessed, and is the
 * type used by the minecraft client instance. It has a backing {@linkplain
 * #activeManager active resource manager} that it delegates to.
 * 
 * <p>It starts with an empty active resource manager, and swaps the active
 * resource manager whenever it {@linkplain #reload reloads}; in addition,
 * {@linkplain #close closing} it will replace the active resource manager
 * with an empty one, and the reloadable manager itself is still accessible to
 * users, as opposed to the lifecycled resource manager.
 */
public class ReloadableResourceManagerImpl implements ReloadableResourceManager, AutoCloseable {
    private LifecycledResourceManager activeManager;
    private final List<ResourceReloader> reloaders = new ArrayList<>();
    private final ResourceType type;

    public ReloadableResourceManagerImpl(ResourceType type) {
        this.type = type;
        this.activeManager = new LifecycledResourceManagerImpl(type, List.of());
    }

    @Override
    public void close() {
        this.activeManager.close();
    }

    /**
     * Registers a reloader to all future reloads on this resource
     * manager.
     */
    @Override
    public void registerReloader(ResourceReloader reloader) {
        this.reloaders.add(reloader);
    }

    /**
     * Swaps the active resource manager with another one backed by the given
     * {@code packs} and start a {@linkplain SimpleResourceReload#start reload}.
     */
    @Override
    public ResourceReload reload(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs) {
        LOGGER.info("Reloading ResourceManager: {}", packs.stream().map(ResourcePack::getName).collect(Collectors.joining(", ")));
        this.activeManager.close();
        this.activeManager = new LifecycledResourceManagerImpl(this.type, packs);
        return SimpleResourceReload.start(this.activeManager, this.reloaders, prepareExecutor, applyExecutor, initialStage, LOGGER.isDebugEnabled());
    }

    @Override
    public Optional<Resource> getResource(Identifier identifier) {
        return this.activeManager.getResource(identifier);
    }

    @Override
    public Set<String> getAllNamespaces() {
        return this.activeManager.getAllNamespaces();
    }

    @Override
    public List<Resource> getAllResources(Identifier id) {
        return this.activeManager.getAllResources(id);
    }

    @Override
    public Map<Identifier, Resource> findResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        return this.activeManager.findResources(startingPath, allowedPathPredicate);
    }

    @Override
    public Map<Identifier, List<Resource>> findAllResources(String startingPath, Predicate<Identifier> allowedPathPredicate) {
        return this.activeManager.findAllResources(startingPath, allowedPathPredicate);
    }

    @Override
    public Stream<ResourcePack> streamResourcePacks() {
        return this.activeManager.streamResourcePacks();
    }
}

