package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.util.Identifier;
import org.apache.commons.lang3.Validate;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class Registries {
    public static final Identifier ROOT_KEY = NAMESPACE.id("root");
    private static final MutableRegistry<MutableRegistry<?>> ROOT = new SimpleRegistry<>(RegistryKey.ofRegistry(ROOT_KEY), Lifecycle.stable());
    public static final Registry<? extends Registry<?>> REGISTRIES = ROOT;

    public static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key) {
        return Registries.create(key, Lifecycle.stable());
    }

    public static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, Identifier defaultId) {
        return Registries.create(key, defaultId, Lifecycle.stable());
    }

    public static <T> DefaultedRegistry<T> createIntrusive(RegistryKey<? extends Registry<T>> key, Identifier defaultId) {
        return Registries.createIntrusive(key, defaultId, Lifecycle.stable());
    }

    public static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle) {
        return Registries.create(key, new SimpleRegistry<>(key, lifecycle, false), lifecycle);
    }

    public static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, Identifier defaultId, Lifecycle lifecycle) {
        return Registries.create(key, new SimpleDefaultedRegistry<>(defaultId, key, lifecycle, false), lifecycle);
    }

    public static <T> DefaultedRegistry<T> createIntrusive(RegistryKey<? extends Registry<T>> key, Identifier defaultId, Lifecycle lifecycle) {
        return Registries.create(key, new SimpleDefaultedRegistry<>(defaultId, key, lifecycle, true), lifecycle);
    }

    public static <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> key, R registry, Lifecycle lifecycle) {
        //noinspection unchecked
        ((MutableRegistry<R>) ROOT).add((RegistryKey<R>) key, registry, lifecycle);
        return registry;
    }

    public static void bootstrap() {
        Registries.validate(REGISTRIES);
    }

    private static <T extends Registry<?>> void validate(Registry<T> registries) {
        registries.forEach(registry -> {
            if (registry instanceof DefaultedRegistry<?> defaultedRegistry) {
                Identifier identifier = defaultedRegistry.getDefaultId();
                Validate.notNull(registry.get(identifier), "Missing default of DefaultedMappedRegistry: " + identifier);
            }
        });
    }
}

