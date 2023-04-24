package net.modificationstation.stationapi.api.tag;

import net.modificationstation.stationapi.api.registry.*;
import net.modificationstation.stationapi.api.resource.IdentifiableResourceReloadListener;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.resource.ResourceReloader;
import net.modificationstation.stationapi.api.util.profiler.Profiler;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

public class TagManagerLoader implements IdentifiableResourceReloadListener {

    public static final Identifier TAGS = MODID.id("tags");

    private final DynamicRegistryManager registryManager;
    private List<RegistryTags<?>> registryTags = List.of();

    public TagManagerLoader(DynamicRegistryManager registryManager) {
        this.registryManager = registryManager;
    }

    public List<RegistryTags<?>> getRegistryTags() {
        return this.registryTags;
    }

    public static String getPath(RegistryKey<? extends Registry<?>> registry) {
        return MODID + "/tags/" + registry.getValue().id;
    }

    @Override
    public CompletableFuture<Void> reload(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        List<? extends CompletableFuture<? extends RegistryTags<?>>> list = this.registryManager.streamAllRegistries().map(entry -> this.buildRequiredGroup(manager, prepareExecutor, entry)).toList();
        CompletableFuture<Void> var10000 = CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
        Objects.requireNonNull(synchronizer);
        //noinspection unchecked
        return var10000.thenCompose(synchronizer::whenPrepared).thenAcceptAsync(void_ -> this.registryTags = (List<RegistryTags<?>>) list.stream().map(CompletableFuture::join).toList(), applyExecutor);
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

