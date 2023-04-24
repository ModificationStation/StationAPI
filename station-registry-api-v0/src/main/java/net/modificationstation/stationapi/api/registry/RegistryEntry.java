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

    boolean matchesId(Identifier id);

    boolean matchesKey(RegistryKey<T> key);

    boolean matches(Predicate<RegistryKey<T>> predicate);

    boolean isIn(TagKey<T> tag);

    Stream<TagKey<T>> streamTags();

    Either<RegistryKey<T>, T> getKeyOrValue();

    Optional<RegistryKey<T>> getKey();

    Type getType();

    boolean ownerEquals(RegistryEntryOwner<T> owner);

    static <T> RegistryEntry<T> of(T value) {
        return new Direct<>(value);
    }

    record Direct<T>(T value) implements RegistryEntry<T> {
        public boolean hasKeyAndValue() {
            return true;
        }

        public boolean matchesId(Identifier id) {
            return false;
        }

        public boolean matchesKey(RegistryKey<T> key) {
            return false;
        }

        public boolean isIn(TagKey<T> tag) {
            return false;
        }

        public boolean matches(Predicate<RegistryKey<T>> predicate) {
            return false;
        }

        public Either<RegistryKey<T>, T> getKeyOrValue() {
            return Either.right(this.value);
        }

        public Optional<RegistryKey<T>> getKey() {
            return Optional.empty();
        }

        public Type getType() {
            return RegistryEntry.Type.DIRECT;
        }

        @Override
        public String toString() {
            return "Direct{" + this.value + "}";
        }

        public boolean ownerEquals(RegistryEntryOwner<T> owner) {
            return true;
        }

        public Stream<TagKey<T>> streamTags() {
            return Stream.of();
        }
    }

    class Reference<T> implements RegistryEntry<T> {
        private final RegistryEntryOwner<T> owner;
        private Set<TagKey<T>> tags = Set.of();
        private final Type referenceType;
        @Nullable
        private RegistryKey<T> registryKey;
        @Nullable
        private T value;

        private Reference(Type referenceType, RegistryEntryOwner<T> owner, @Nullable RegistryKey<T> registryKey, @Nullable T value) {
            this.owner = owner;
            this.referenceType = referenceType;
            this.registryKey = registryKey;
            this.value = value;
        }

        public static <T> Reference<T> standAlone(RegistryEntryOwner<T> owner, RegistryKey<T> registryKey) {
            return new Reference<>(RegistryEntry.Reference.Type.STAND_ALONE, owner, registryKey, null);
        }

        /** @deprecated */
        @Deprecated
        public static <T> Reference<T> intrusive(RegistryEntryOwner<T> owner, @Nullable T value) {
            return new Reference<>(RegistryEntry.Reference.Type.INTRUSIVE, owner, null, value);
        }

        public RegistryKey<T> registryKey() {
            if (this.registryKey == null)
                throw new IllegalStateException("Trying to access unbound value '" + this.value + "' from registry " + this.owner);
            else return this.registryKey;
        }

        public T value() {
            if (this.value == null)
                throw new IllegalStateException("Trying to access unbound value '" + this.registryKey + "' from registry " + this.owner);
            else return this.value;
        }

        public boolean matchesId(Identifier id) {
            return this.registryKey().getValue().equals(id);
        }

        public boolean matchesKey(RegistryKey<T> key) {
            return this.registryKey() == key;
        }

        public boolean isIn(TagKey<T> tag) {
            return this.tags.contains(tag);
        }

        public boolean matches(Predicate<RegistryKey<T>> predicate) {
            return predicate.test(this.registryKey());
        }

        public boolean ownerEquals(RegistryEntryOwner<T> owner) {
            return this.owner.ownerEquals(owner);
        }

        public Either<RegistryKey<T>, T> getKeyOrValue() {
            return Either.left(this.registryKey());
        }

        public Optional<RegistryKey<T>> getKey() {
            return Optional.of(this.registryKey());
        }

        public RegistryEntry.Type getType() {
            return RegistryEntry.Type.REFERENCE;
        }

        public boolean hasKeyAndValue() {
            return this.registryKey != null && this.value != null;
        }

        void setRegistryKey(RegistryKey<T> registryKey) {
            if (this.registryKey != null && registryKey != this.registryKey)
                throw new IllegalStateException("Can't change holder key: existing=" + this.registryKey + ", new=" + registryKey);
            else this.registryKey = registryKey;
        }

        void setValue(T value) {
            if (this.referenceType == RegistryEntry.Reference.Type.INTRUSIVE && this.value != value)
                throw new IllegalStateException("Can't change holder " + this.registryKey + " value: existing=" + this.value + ", new=" + value);
            else this.value = value;
        }

        void setTags(Collection<TagKey<T>> tags) {
            this.tags = Set.copyOf(tags);
        }

        public Stream<TagKey<T>> streamTags() {
            return this.tags.stream();
        }

        public String toString() {
            return "Reference{" + this.registryKey + "=" + this.value + "}";
        }

        enum Type {
            STAND_ALONE,
            INTRUSIVE
        }
    }

    enum Type {
        REFERENCE,
        DIRECT
    }
}
