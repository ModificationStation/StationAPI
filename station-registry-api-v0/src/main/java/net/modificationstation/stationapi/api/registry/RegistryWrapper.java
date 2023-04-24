package net.modificationstation.stationapi.api.registry;

import com.mojang.serialization.Lifecycle;
import net.modificationstation.stationapi.api.tag.TagKey;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A read-only wrapper of a registry.
 */
public interface RegistryWrapper<T> extends RegistryEntryLookup<T> {
    /**
     * {@return a stream of registry keys defined in the wrapped registry}
     * 
     * @see Registry#getKeys
     */
    Stream<RegistryEntry.Reference<T>> streamEntries();

    default Stream<RegistryKey<T>> streamKeys() {
        return this.streamEntries().map(RegistryEntry.Reference::registryKey);
    }

    /**
     * @see Registry#streamTags
     */
    Stream<RegistryEntryList.Named<T>> streamTags();

    default Stream<TagKey<T>> streamTagKeys() {
        return this.streamTags().map(RegistryEntryList.Named::getTag);
    }

    default RegistryWrapper<T> filter(final Predicate<T> filter) {
        return new Delegating<>(this) {

            @Override
            public Optional<RegistryEntry.Reference<T>> getOptional(RegistryKey<T> key) {
                return this.baseWrapper.getOptional(key).filter(entry -> filter.test(entry.value()));
            }

            @Override
            public Stream<RegistryEntry.Reference<T>> streamEntries() {
                return this.baseWrapper.streamEntries().filter(entry -> filter.test(entry.value()));
            }
        };
    }

    interface WrapperLookup {
        <T> Optional<Impl<T>> getOptionalWrapper(RegistryKey<? extends Registry<? extends T>> var1);

        default <T> Impl<T> getWrapperOrThrow(RegistryKey<? extends Registry<? extends T>> registryRef) {
            return this.getOptionalWrapper(registryRef).orElseThrow(() -> new IllegalStateException("Registry " + registryRef.getValue() + " not found"));
        }

        default RegistryEntryLookup.RegistryLookup createRegistryLookup() {
            return new RegistryEntryLookup.RegistryLookup(){

                @Override
                public <T> Optional<RegistryEntryLookup<T>> getOptional(RegistryKey<? extends Registry<? extends T>> registryRef) {
                    return WrapperLookup.this.getOptionalWrapper(registryRef).map(lookup -> lookup);
                }
            };
        }

        static WrapperLookup of(Stream<Impl<?>> wrappers) {
            final Map<RegistryKey<?>, Impl<?>> map = wrappers.collect(Collectors.toUnmodifiableMap(Impl::getRegistryKey, Function.identity()));
            return new WrapperLookup(){

                @Override
                public <T> Optional<Impl<T>> getOptionalWrapper(RegistryKey<? extends Registry<? extends T>> registryRef) {
                    //noinspection unchecked
                    return Optional.ofNullable((Impl<T>) map.get(registryRef));
                }
            };
        }
    }

    class Delegating<T> implements RegistryWrapper<T> {
        protected final RegistryWrapper<T> baseWrapper;

        public Delegating(RegistryWrapper<T> baseWrapper) {
            this.baseWrapper = baseWrapper;
        }

        @Override
        public Optional<RegistryEntry.Reference<T>> getOptional(RegistryKey<T> key) {
            return this.baseWrapper.getOptional(key);
        }

        @Override
        public Stream<RegistryEntry.Reference<T>> streamEntries() {
            return this.baseWrapper.streamEntries();
        }

        @Override
        public Optional<RegistryEntryList.Named<T>> getOptional(TagKey<T> tag) {
            return this.baseWrapper.getOptional(tag);
        }

        @Override
        public Stream<RegistryEntryList.Named<T>> streamTags() {
            return this.baseWrapper.streamTags();
        }
    }

    interface Impl<T> extends RegistryWrapper<T>, RegistryEntryOwner<T> {
        RegistryKey<? extends Registry<? extends T>> getRegistryKey();

        Lifecycle getLifecycle();

        abstract class Delegating<T> implements Impl<T> {
            protected abstract Impl<T> getBase();

            @Override
            public RegistryKey<? extends Registry<? extends T>> getRegistryKey() {
                return this.getBase().getRegistryKey();
            }

            @Override
            public Lifecycle getLifecycle() {
                return this.getBase().getLifecycle();
            }

            @Override
            public Optional<RegistryEntry.Reference<T>> getOptional(RegistryKey<T> key) {
                return this.getBase().getOptional(key);
            }

            @Override
            public Stream<RegistryEntry.Reference<T>> streamEntries() {
                return this.getBase().streamEntries();
            }

            @Override
            public Optional<RegistryEntryList.Named<T>> getOptional(TagKey<T> tag) {
                return this.getBase().getOptional(tag);
            }

            @Override
            public Stream<RegistryEntryList.Named<T>> streamTags() {
                return this.getBase().streamTags();
            }
        }
    }
}

