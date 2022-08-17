package net.modificationstation.stationapi.api.registry;

import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;

import java.util.Map;
import java.util.function.Supplier;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

/**
 * Stores a few hardcoded registries with builtin values for datapack-loadable registries,
 * from which a registry tracker can create a new dynamic registry.
 * 
 * <p>Note that these registries do not contain the actual entries that the server has,
 * for that you will need to access it from {@link net.modificationstation.stationapi.api.registry.DynamicRegistryManager}.
 *
 * @see net.modificationstation.stationapi.api.registry.DynamicRegistryManager#get(RegistryKey)
 */
public class BuiltinRegistries {
    private static final Map<Identifier, Supplier<? extends RegistryEntry<?>>> DEFAULT_VALUE_SUPPLIERS = Maps.newLinkedHashMap();
    private static final MutableRegistry<Registry<?>> ROOT = new SimpleRegistry<>(RegistryKey.ofRegistry(Identifier.of("root")), Lifecycle.experimental(), null);
    public static final Registry<Registry<?>> REGISTRIES = ROOT;
    public static final DynamicRegistryManager DYNAMIC_REGISTRY_MANAGER;

    private static <T> Registry<T> addRegistry(RegistryKey<Registry<T>> registryRef, Initializer<T> initializer) {
        return BuiltinRegistries.addRegistry(registryRef, Lifecycle.stable(), initializer);
    }

    private static <T> Registry<T> addRegistry(RegistryKey<Registry<T>> registryRef, Lifecycle lifecycle, Initializer<T> initializer) {
        return BuiltinRegistries.addRegistry(registryRef, new SimpleRegistry<>(registryRef, lifecycle, null), initializer, lifecycle);
    }

    private static <T, R extends MutableRegistry<T>> R addRegistry(RegistryKey<Registry<T>> registryRef, R registry, Initializer<T> initializer, Lifecycle lifecycle) {
        Identifier identifier = registryRef.getValue();
        DEFAULT_VALUE_SUPPLIERS.put(identifier, () -> initializer.run(registry));
        //noinspection unchecked
        ROOT.add((RegistryKey<Registry<?>>) (RegistryKey<?>) registryRef, registry, lifecycle);
        return registry;
    }

    public static <V extends T, T> RegistryEntry<V> addCasted(Registry<T> registry, String id, V value) {
        //noinspection unchecked
        return (RegistryEntry<V>) BuiltinRegistries.add(registry, Identifier.of(id), value);
    }

    public static <T> RegistryEntry<T> add(Registry<T> registry, String id, T object) {
        return BuiltinRegistries.add(registry, Identifier.of(id), object);
    }

    public static <T> RegistryEntry<T> add(Registry<T> registry, Identifier id, T object) {
        return BuiltinRegistries.add(registry, RegistryKey.of(registry.getKey(), id), object);
    }

    public static <T> RegistryEntry<T> add(Registry<T> registry, RegistryKey<T> key, T object) {
        return ((MutableRegistry<T>) registry).add(key, object, Lifecycle.stable());
    }

    public static void init() {
    }

    static {
        DEFAULT_VALUE_SUPPLIERS.forEach((id, supplier) -> {
            if (!supplier.get().hasKeyAndValue()) {
                LOGGER.error("Unable to bootstrap registry '{}'", id);
            }
        });
        Registry.validate(ROOT);
        DYNAMIC_REGISTRY_MANAGER = DynamicRegistryManager.of(REGISTRIES);
    }

    @FunctionalInterface
    interface Initializer<T> {
        RegistryEntry<? extends T> run(Registry<T> var1);
    }
}

