package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
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
            return RegistryEntry.Type.DIRECT;
        }

        @Override
        public String toString() {
            return "Direct{" + this.value + "}";
        }

        @Override
        public boolean ownerEquals(RegistryEntryOwner<T> owner) {
            return true;
        }

        @Override
        public Stream<TagKey<T>> streamTags() {
            return Stream.of();
        }
    }

    class Reference<T> implements RegistryEntry<T> {
        private final RegistryEntryOwner<T> owner;
        private Set<TagKey<T>> tags = Set.of();
        private final Type referenceType;
        /**
         * Isn't actually used for storing the raw ID.
         * The only purpose for this field is to be able
         * to override raw IDs from {@link BlockBase}
         * or {@link ItemBase} constructors before actually
         * registering them.
         */
        private int reservedRawId;
        @Nullable
        private RegistryKey<T> registryKey;
        @Nullable
        private T value;

        private Reference(Type referenceType, RegistryEntryOwner<T> owner, int reservedRawId, @Nullable RegistryKey<T> registryKey, @Nullable T value) {
            this.owner = owner;
            this.referenceType = referenceType;
            this.reservedRawId = reservedRawId;
            this.registryKey = registryKey;
            this.value = value;
        }

        public static <T> Reference<T> standAlone(RegistryEntryOwner<T> owner, RegistryKey<T> registryKey) {
            return new Reference<>(RegistryEntry.Reference.Type.STAND_ALONE, owner, -1, registryKey, null);
        }

        /** @deprecated */
        @Deprecated
        public static <T> Reference<T> intrusive(RegistryEntryOwner<T> owner, @Nullable T value) {
            return new Reference<>(RegistryEntry.Reference.Type.INTRUSIVE, owner, -1, null, value);
        }

        static <T> Reference<T> intrusive(RegistryEntryOwner<T> owner, int reservedRawId, @Nullable T value) {
            return new Reference<>(Type.INTRUSIVE, owner, reservedRawId, null, value);
        }

        public int reservedRawId() {
            if (this.reservedRawId < 0)
                throw new IllegalStateException("Trying to access unbound value '" + (value == null ? registryKey : value) + "' from registry " + this.owner);
            else return reservedRawId;
        }

        public RegistryKey<T> registryKey() {
            if (this.registryKey == null)
                throw new IllegalStateException("Trying to access unbound value '" + this.value + "' from registry " + this.owner);
            else return registryKey;
        }

        @Override
        public T value() {
            if (value == null)
                throw new IllegalStateException("Trying to access unbound value '" + registryKey + "' from registry " + owner);
            else return value;
        }

        @Override
        public boolean matchesId(Identifier id) {
            return registryKey().getValue().equals(id);
        }

        @Override
        public boolean matchesKey(RegistryKey<T> key) {
            return registryKey() == key;
        }

        @Override
        public boolean isIn(TagKey<T> tag) {
            return tags.contains(tag);
        }

        @Override
        public boolean matches(Predicate<RegistryKey<T>> predicate) {
            return predicate.test(registryKey());
        }

        @Override
        public boolean ownerEquals(RegistryEntryOwner<T> owner) {
            return this.owner.ownerEquals(owner);
        }

        @Override
        public Either<RegistryKey<T>, T> getKeyOrValue() {
            return Either.left(registryKey());
        }

        @Override
        public Optional<RegistryKey<T>> getKey() {
            return Optional.of(registryKey());
        }

        @Override
        public RegistryEntry.Type getType() {
            return RegistryEntry.Type.REFERENCE;
        }

        boolean hasRawId() {
            return reservedRawId >= 0;
        }

        @Override
        public boolean hasKeyAndValue() {
            return registryKey != null && value != null;
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

        @Override
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
