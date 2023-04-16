package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Either;
import net.modificationstation.stationapi.api.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface RegistryEntry<T> {

    T value();

    boolean hasKeyAndValue();

    boolean matchesId(Identifier var1);

    boolean matchesKey(RegistryKey<T> var1);

    boolean matches(Predicate<RegistryKey<T>> var1);

    boolean isIn(TagKey<T> var1);

    Stream<TagKey<T>> streamTags();

    Either<RegistryKey<T>, T> getKeyOrValue();

    Optional<RegistryKey<T>> getKey();

    Type getType();

    boolean matchesRegistry(Registry<T> var1);

    static <T> RegistryEntry<T> of(T value) {
        return new Direct<>(value);
    }

    static <T> RegistryEntry<T> upcast(RegistryEntry<? extends T> entry) {
        //noinspection unchecked
        return (RegistryEntry<T>) entry;
    }

    record Direct<T>(T value) implements RegistryEntry<T> {

        @Override
        public boolean hasKeyAndValue() {
            return true;
        }

        @Override
        public boolean matchesId(Identifier id) {
            return false;
        }

        @Override
        public boolean matchesKey(RegistryKey<T> key) {
            return false;
        }

        @Override
        public boolean isIn(TagKey<T> tag) {
            return false;
        }

        @Override
        public boolean matches(Predicate<RegistryKey<T>> predicate) {
            return false;
        }

        @Override
        public Either<RegistryKey<T>, T> getKeyOrValue() {
            return Either.right(this.value);
        }

        @Override
        public Optional<RegistryKey<T>> getKey() {
            return Optional.empty();
        }

        @Override
        public Type getType() {
            return Type.DIRECT;
        }

        @Override
        public String toString() {
            return "Direct{" + this.value + "}";
        }

        @Override
        public boolean matchesRegistry(Registry<T> registry) {
            return true;
        }

        @Override
        public Stream<TagKey<T>> streamTags() {
            //noinspection unchecked
            return Stream.of(new TagKey[0]);
        }
    }

    class Reference<T>
    implements RegistryEntry<T> {
        private final Registry<T> registry;
        private Set<TagKey<T>> tags = Set.of();
        private final Type referenceType;
        @Nullable
        private RegistryKey<T> registryKey;
        @Nullable
        private T value;

        private Reference(Type referenceType, Registry<T> registry, @Nullable RegistryKey<T> registryKey, @Nullable T value) {
            this.registry = registry;
            this.referenceType = referenceType;
            this.registryKey = registryKey;
            this.value = value;
        }

        public static <T> Reference<T> standAlone(Registry<T> registry, RegistryKey<T> registryKey) {
            return new Reference<>(Type.STAND_ALONE, registry, registryKey, null);
        }

        @Deprecated
        public static <T> Reference<T> intrusive(Registry<T> registry, @Nullable T registryKey) {
            return new Reference<>(Type.INTRUSIVE, registry, null, registryKey);
        }

        public RegistryKey<T> registryKey() {
            if (this.registryKey == null) {
                throw new IllegalStateException("Trying to access unbound value '" + this.value + "' from registry " + this.registry);
            }
            return this.registryKey;
        }

        @Override
        public T value() {
            if (this.value == null) {
                throw new IllegalStateException("Trying to access unbound value '" + this.registryKey + "' from registry " + this.registry);
            }
            return this.value;
        }

        @Override
        public boolean matchesId(Identifier id) {
            return this.registryKey().getValue().equals(id);
        }

        @Override
        public boolean matchesKey(RegistryKey<T> key) {
            return this.registryKey() == key;
        }

        @Override
        public boolean isIn(TagKey<T> tag) {
            return this.tags.contains(tag);
        }

        @Override
        public boolean matches(Predicate<RegistryKey<T>> predicate) {
            return predicate.test(this.registryKey());
        }

        @Override
        public boolean matchesRegistry(Registry<T> registry) {
            return this.registry == registry;
        }

        @Override
        public Either<RegistryKey<T>, T> getKeyOrValue() {
            return Either.left(this.registryKey());
        }

        @Override
        public Optional<RegistryKey<T>> getKey() {
            return Optional.of(this.registryKey());
        }

        @Override
        public RegistryEntry.Type getType() {
            return RegistryEntry.Type.REFERENCE;
        }

        @Override
        public boolean hasKeyAndValue() {
            return this.registryKey != null && this.value != null;
        }

        void setKeyAndValue(RegistryKey<T> key, T value) {
            if (this.registryKey != null && key != this.registryKey) {
                throw new IllegalStateException("Can't change holder key: existing=" + this.registryKey + ", new=" + key);
            }
            if (this.referenceType == Type.INTRUSIVE && this.value != value) {
                throw new IllegalStateException("Can't change holder " + key + " value: existing=" + this.value + ", new=" + value);
            }
            this.registryKey = key;
            this.value = value;
        }

        void setTags(Collection<TagKey<T>> tags) {
            this.tags = Set.copyOf(tags);
        }

        @Override
        public Stream<TagKey<T>> streamTags() {
            return this.tags.stream();
        }

        public String toString() {
            return "Reference{" + this.registryKey + "=" + this.value + "}";
        }

        enum Type {
            STAND_ALONE,
            INTRUSIVE;

        }
    }

    enum Type {
        REFERENCE,
        DIRECT;

    }
}

