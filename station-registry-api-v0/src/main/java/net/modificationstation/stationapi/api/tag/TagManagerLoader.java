package net.modificationstation.stationapi.api.tag;

import lombok.val;
import net.modificationstation.stationapi.api.registry.*;
import net.modificationstation.stationapi.api.resource.IdentifiableResourceReloadListener;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.resource.ResourceReloader;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.profiler.Profiler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class TagManagerLoader implements IdentifiableResourceReloadListener {
    public static final Identifier TAGS = NAMESPACE.id("tags");

    private final DynamicRegistryManager registryManager;
    private List<RegistryTags<?>> registryTags = List.of();

    public TagManagerLoader(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    public List<RegistryTags<?>> getRegistryTags() {
        return this.registryTags;
    }

    public static String getPath(RegistryKey<? extends Registry<?>> registry) {
        return NAMESPACE + "/tags/" + registry.getValue().path;
    }

    @Override
    public CompletableFuture<Void> reload(
            ResourceReloader.Synchronizer synchronizer,
            ResourceManager manager,
            Profiler prepareProfiler,
            Profiler applyProfiler,
            Executor prepareExecutor,
            Executor applyExecutor
    ) {
        prepareProfiler.startTick();
        val list = this.registryManager.streamAllRegistries().map(entry -> this.buildRequiredGroup(manager, prepareExecutor, entry)).toList();
        //noinspection unchecked
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new))
                .thenRunAsync(prepareProfiler::endTick, prepareExecutor)
                .thenCompose(Objects.requireNonNull(synchronizer)::whenPrepared)
                .thenRunAsync(applyProfiler::startTick, applyExecutor)
                .thenAcceptAsync(void_ -> this.registryTags = (List<RegistryTags<?>>) (List<? extends RegistryTags<?>>) list.stream().map(CompletableFuture::join).toList(), applyExecutor)
                .thenRunAsync(() -> {
                    applyProfiler.push("populate");
                    registryTags.forEach(tags -> repopulateTags(registryManager, tags));
                    applyProfiler.pop();
                }, applyExecutor)
                .thenRunAsync(applyProfiler::endTick, applyExecutor);
    }

    private static <T> void repopulateTags(DynamicRegistryManager dynamicRegistryManager, TagManagerLoader.RegistryTags<T> tags) {
        RegistryKey<? extends Registry<T>> registryKey = tags.key();
        Map<TagKey<T>, List<RegistryEntry<T>>> map = tags.tags().entrySet().stream().collect(Collectors.toUnmodifiableMap(entry -> TagKey.of(registryKey, entry.getKey()), entry -> List.copyOf(entry.getValue())));
        dynamicRegistryManager.get(registryKey).populateTags(map);
    }

    private <T> CompletableFuture<RegistryTags<T>> buildRequiredGroup(ResourceManager resourceManager, Executor prepareExecutor, DynamicRegistryManager.Entry<T> requirement) {
        RegistryKey<? extends Registry<T>> registryKey = requirement.key();
        Registry<T> registry = requirement.value();
        TagGroupLoader<RegistryEntry<T>> tagGroupLoader = new TagGroupLoader<>(id -> registry.getEntry(RegistryKey.of(registryKey, id)), getPath(registryKey));
        return CompletableFuture.supplyAsync(() -> new RegistryTags<>(registryKey, tagGroupLoader.load(resourceManager)), prepareExecutor);
    }

    @Override
    public Identifier getId() {
        return TAGS;
    }

    public record RegistryTags<T>(RegistryKey<? extends Registry<T>> key, Map<Identifier, Collection<RegistryEntry<T>>> tags) { }
}

