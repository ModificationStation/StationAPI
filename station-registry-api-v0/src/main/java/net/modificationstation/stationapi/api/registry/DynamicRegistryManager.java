package net.modificationstation.stationapi.api.registry;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Lifecycle;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A manager of dynamic registries. It allows users to access non-hardcoded
 * registries reliably.
 * 
 * <p>The {@link DynamicRegistryManager.ImmutableImpl}
 * class serves as an immutable implementation of any particular collection
 * or configuration of dynamic registries.
 */
public interface DynamicRegistryManager extends RegistryWrapper.WrapperLookup {
    Immutable EMPTY = new ImmutableImpl(Map.of()).toImmutable();

    <E> Optional<Registry<E>> getOptional(RegistryKey<? extends Registry<? extends E>> var1);

    @Override
    default <T> Optional<RegistryWrapper.Impl<T>> getOptionalWrapper(RegistryKey<? extends Registry<? extends T>> registryRef) {
        return this.getOptional(registryRef).map(Registry::getReadOnlyWrapper);
    }

    /**
     * Retrieves a registry from this manager, or throws an exception when the registry
     * does not exist.
     * 
     * @throws IllegalStateException if the registry does not exist
     */
    default <E> Registry<E> get(RegistryKey<? extends Registry<? extends E>> key) {
        return this.getOptional(key).orElseThrow(() -> new IllegalStateException("Missing registry: " + key));
    }

    Stream<Entry<?>> streamAllRegistries();

    static Immutable of(final Registry<? extends Registry<?>> registries) {
        return new Immutable(){

            @Override
            public <T> Optional<Registry<T>> getOptional(RegistryKey<? extends Registry<? extends T>> key) {
                //noinspection unchecked
                return ((Registry<Registry<T>>) registries).getOrEmpty((RegistryKey<Registry<T>>) key);
            }

            @Override
            public Stream<Entry<?>> streamAllRegistries() {
                return registries.getEntrySet().stream().map(Entry::of);
            }

            @Override
            public Immutable toImmutable() {
                return this;
            }
        };
    }

    default Immutable toImmutable() {
        class Immutablized extends ImmutableImpl implements Immutable {
            protected Immutablized(Stream<Entry<?>> entryStream) {
                super(entryStream);
            }
        }
        return new Immutablized(this.streamAllRegistries().map(Entry::freeze));
    }

    default Lifecycle getRegistryLifecycle() {
        return this.streamAllRegistries().map(entry -> entry.value.getLifecycle()).reduce(Lifecycle.stable(), Lifecycle::add);
    }

    record Entry<T>(RegistryKey<? extends Registry<T>> key, Registry<T> value) {
        private static <T, R extends Registry<? extends T>> Entry<T> of(Map.Entry<? extends RegistryKey<? extends Registry<?>>, R> entry) {
            return Entry.of(entry.getKey(), entry.getValue());
        }

        private static <T> Entry<T> of(RegistryKey<? extends Registry<?>> key, Registry<?> value) {
            //noinspection unchecked
            return new Entry<>((RegistryKey<? extends Registry<T>>) key, (Registry<T>) value);
        }

        private Entry<T> freeze() {
            return new Entry<>(this.key, this.value.freeze());
        }
    }

    class ImmutableImpl implements DynamicRegistryManager {
        private final Map<? extends RegistryKey<? extends Registry<?>>, ? extends Registry<?>> registries;

        public ImmutableImpl(List<? extends Registry<?>> registries) {
            this.registries = registries.stream().collect(Collectors.toUnmodifiableMap(Registry::getKey, registry -> registry));
        }

        public ImmutableImpl(Map<? extends RegistryKey<? extends Registry<?>>, ? extends Registry<?>> registries) {
            this.registries = Map.copyOf(registries);
        }

        public ImmutableImpl(Stream<Entry<?>> entryStream) {
            this.registries = entryStream.collect(ImmutableMap.toImmutableMap(Entry::key, Entry::value));
        }

        @Override
        public <E> Optional<Registry<E>> getOptional(RegistryKey<? extends Registry<? extends E>> key) {
            //noinspection unchecked
            return Optional.ofNullable(this.registries.get(key)).map(registry -> (Registry<E>) registry);
        }

        @Override
        public Stream<Entry<?>> streamAllRegistries() {
            return this.registries.entrySet().stream().map(Entry::of);
        }
    }

    interface Immutable extends DynamicRegistryManager {}
}

