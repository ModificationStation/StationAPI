package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Util;
import org.apache.commons.lang3.Validate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;
import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

public class Registries {
    private static final Map<Identifier, Supplier<?>> DEFAULT_ENTRIES = new LinkedHashMap<>();
    public static final Identifier ROOT_KEY = NAMESPACE.id("root");
    private static final MutableRegistry<MutableRegistry<?>> ROOT = new SimpleRegistry<>(RegistryKey.ofRegistry(ROOT_KEY), Lifecycle.stable());
    public static final Registry<? extends Registry<?>> REGISTRIES = ROOT;

    public static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Initializer<T> initializer) {
        return Registries.create(key, Lifecycle.stable(), initializer);
    }

    public static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, Identifier defaultId, Initializer<T> initializer) {
        return Registries.create(key, defaultId, Lifecycle.stable(), initializer);
    }

    public static <T> DefaultedRegistry<T> createIntrusive(RegistryKey<? extends Registry<T>> key, Identifier defaultId, Initializer<T> initializer) {
        return Registries.createIntrusive(key, defaultId, Lifecycle.stable(), initializer);
    }

    public static <T> Registry<T> create(RegistryKey<? extends Registry<T>> key, Lifecycle lifecycle, Initializer<T> initializer) {
        return Registries.create(key, new SimpleRegistry<>(key, lifecycle, false), initializer, lifecycle);
    }

    public static <T> DefaultedRegistry<T> create(RegistryKey<? extends Registry<T>> key, Identifier defaultId, Lifecycle lifecycle, Initializer<T> initializer) {
        return Registries.create(key, new SimpleDefaultedRegistry<>(defaultId, key, lifecycle, false), initializer, lifecycle);
    }

    public static <T> DefaultedRegistry<T> createIntrusive(RegistryKey<? extends Registry<T>> key, Identifier defaultId, Lifecycle lifecycle, Initializer<T> initializer) {
        return Registries.create(key, new SimpleDefaultedRegistry<>(defaultId, key, lifecycle, true), initializer, lifecycle);
    }

    public static <T, R extends MutableRegistry<T>> R create(RegistryKey<? extends Registry<T>> key, R registry, Initializer<T> initializer, Lifecycle lifecycle) {
        Identifier identifier = key.getValue();
        DEFAULT_ENTRIES.put(identifier, () -> initializer.run(registry));
        //noinspection unchecked
        ((MutableRegistry<R>) ROOT).add((RegistryKey<R>) key, registry, lifecycle);
        return registry;
    }

    public static void bootstrap() {
        Registries.init();
        Registries.freezeRegistries();
        Registries.validate(REGISTRIES);
    }

    private static void init() {
        DEFAULT_ENTRIES.forEach((id, initializer) -> {
            if (initializer.get() == null) LOGGER.error("Unable to bootstrap registry '{}'", id);
        });
    }

    private static void freezeRegistries() {
        REGISTRIES.freeze();
        for (Registry<?> registry : REGISTRIES) registry.freeze();
    }

    private static <T extends Registry<?>> void validate(Registry<T> registries) {
        registries.forEach(registry -> {
            if (registry.getIds().isEmpty())
                Util.error("Registry '" + registries.getId(registry) + "' was empty after loading");
            if (registry instanceof DefaultedRegistry<?> defaultedRegistry) {
                Identifier identifier = defaultedRegistry.getDefaultId();
                Validate.notNull(registry.get(identifier), "Missing default of DefaultedMappedRegistry: " + identifier);
            }
        });
    }

    @FunctionalInterface
    public interface Initializer<T> {
        T run(Registry<T> var1);
    }
}

