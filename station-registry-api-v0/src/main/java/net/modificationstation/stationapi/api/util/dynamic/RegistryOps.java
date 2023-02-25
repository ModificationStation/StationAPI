package net.modificationstation.stationapi.api.util.dynamic;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.modificationstation.stationapi.api.registry.DynamicRegistryManager;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import java.util.Optional;

public class RegistryOps<T>
extends ForwardingDynamicOps<T> {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<RegistryLoader.LoaderAccess> loaderAccess;
    private final DynamicRegistryManager registryManager;
    private final DynamicOps<JsonElement> entryOps;

    public static <T> RegistryOps<T> of(DynamicOps<T> delegate, DynamicRegistryManager registryManager) {
        return new RegistryOps<>(delegate, registryManager, Optional.empty());
    }

    public static <T> RegistryOps<T> ofLoaded(DynamicOps<T> ops, DynamicRegistryManager.Mutable registryManager, ResourceManager resourceManager) {
        return RegistryOps.ofLoaded(ops, registryManager, EntryLoader.resourceBacked(resourceManager));
    }

    public static <T> RegistryOps<T> ofLoaded(DynamicOps<T> ops, DynamicRegistryManager.Mutable registryManager, EntryLoader entryLoader) {
        RegistryLoader registryLoader = new RegistryLoader(entryLoader);
        RegistryOps<T> registryOps = new RegistryOps<>(ops, registryManager, Optional.of(registryLoader.createAccess(registryManager)));
        DynamicRegistryManager.load(registryManager, registryOps.getEntryOps(), registryLoader);
        return registryOps;
    }

    private RegistryOps(DynamicOps<T> delegate, DynamicRegistryManager dynamicRegistryManager, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<RegistryLoader.LoaderAccess> loaderAccess) {
        super(delegate);
        this.loaderAccess = loaderAccess;
        this.registryManager = dynamicRegistryManager;
        //noinspection unchecked
        this.entryOps = delegate == JsonOps.INSTANCE ? (DynamicOps<JsonElement>) this : new RegistryOps<>(JsonOps.INSTANCE, dynamicRegistryManager, loaderAccess);
    }

    public <E> Optional<? extends Registry<E>> getRegistry(RegistryKey<? extends Registry<? extends E>> key) {
        return this.registryManager.getOptional(key);
    }

    public Optional<RegistryLoader.LoaderAccess> getLoaderAccess() {
        return this.loaderAccess;
    }

    public DynamicOps<JsonElement> getEntryOps() {
        return this.entryOps;
    }

    public static <E> MapCodec<Registry<E>> createRegistryCodec(RegistryKey<? extends Registry<? extends E>> registryRef) {
        return Codecs.createContextRetrievalCodec((ops) -> {
            if (ops instanceof RegistryOps<?> registryOps) {
                //noinspection unchecked
                return (DataResult<Registry<E>>) registryOps.getRegistry(registryRef).map((registry) -> DataResult.success(registry, registry.getLifecycle())).orElseGet(() -> DataResult.error(() -> "Unknown registry: " + registryRef));
            } else {
                return DataResult.error(() -> "Not a registry ops");
            }
        });
    }
}

