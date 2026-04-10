package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Either;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Identifier;
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

    Set<TagKey<T>> getTags();

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

        @Override
        public Set<TagKey<T>> getTags() {
            return Set.of();
        }
    }

    abstract class Reference<ENTRY> implements RegistryEntry<ENTRY> {
        final RegistryEntryOwner<ENTRY> owner;
        private Set<TagKey<ENTRY>> tags = Set.of();

        private Reference(RegistryEntryOwner<ENTRY> owner) {
            this.owner = owner;
        }

        public abstract RegistryKey<ENTRY> registryKey();

        @Override
        public boolean matchesId(Identifier id) {
            return registryKey().getValue().equals(id);
        }

        @Override
        public boolean matchesKey(RegistryKey<ENTRY> key) {
            return registryKey() == key;
        }

        @Override
        public boolean isIn(TagKey<ENTRY> tag) {
            return tags.contains(tag);
        }

        @Override
        public boolean matches(Predicate<RegistryKey<ENTRY>> predicate) {
            return predicate.test(registryKey());
        }

        @Override
        public boolean ownerEquals(RegistryEntryOwner<ENTRY> owner) {
            return this.owner.ownerEquals(owner);
        }

        @Override
        public Either<RegistryKey<ENTRY>, ENTRY> getKeyOrValue() {
            return Either.left(registryKey());
        }

        @Override
        public Optional<RegistryKey<ENTRY>> getKey() {
            return Optional.of(registryKey());
        }

        @Override
        public Type getType() {
            return Type.REFERENCE;
        }

        abstract void setRegistryKey(RegistryKey<ENTRY> registryKey);

        abstract void setValue(ENTRY value);

        void setTags(Collection<TagKey<ENTRY>> tags) {
            this.tags = Set.copyOf(tags);
        }

        @Override
        public Stream<TagKey<ENTRY>> streamTags() {
            return this.tags.stream();
        }

        @Override
        public Set<TagKey<ENTRY>> getTags() {
            return tags;
        }

        public String toString() {
            return "Reference{" + registryKey() + "=" + value() + "}";
        }

        static <ENTRY> Reference<ENTRY> standAlone(RegistryEntryOwner<ENTRY> owner, RegistryKey<ENTRY> registryKey) {
            return new StandAlone<>(owner, registryKey);
        }

        static <ENTRY> Reference<ENTRY> intrusive(RegistryEntryOwner<ENTRY> owner, ENTRY value) {
            return new Intrusive<>(owner, value);
        }

        static <ENTRY> Reference<ENTRY> intrusive(RegistryEntryOwner<ENTRY> owner, ENTRY value, int reservedRawId) {
            return new IntrusiveReserved<>(owner, value, reservedRawId);
        }

        public static class StandAlone<ENTRY> extends Reference<ENTRY> {
            private final RegistryKey<ENTRY> registryKey;
            private @Nullable ENTRY value;

            private StandAlone(RegistryEntryOwner<ENTRY> owner, RegistryKey<ENTRY> registryKey) {
                super(owner);
                this.registryKey = registryKey;
            }

            @Override
            public RegistryKey<ENTRY> registryKey() {
                return registryKey;
            }

            @Override
            public ENTRY value() {
                if (value == null)
                    throw new IllegalStateException("Trying to access unbound value '" + registryKey + "' from registry " + owner);
                return value;
            }

            @Override
            public boolean hasKeyAndValue() {
                return registryKey != null && value != null;
            }

            @Override
            void setRegistryKey(RegistryKey<ENTRY> registryKey) {
                if (registryKey != this.registryKey)
                    throw new IllegalStateException("Can't change holder key: existing=" + this.registryKey + ", new=" + registryKey);
            }

            @Override
            void setValue(@Nullable ENTRY value) {
                this.value = value;
            }
        }

        public static class Intrusive<ENTRY> extends Reference<ENTRY> {
            private @Nullable RegistryKey<ENTRY> registryKey;
            private final ENTRY value;

            private Intrusive(RegistryEntryOwner<ENTRY> owner, ENTRY value) {
                super(owner);
                this.value = value;
            }

            @Override
            public RegistryKey<ENTRY> registryKey() {
                if (this.registryKey == null)
                    throw new IllegalStateException("Trying to access unbound value '" + this.value + "' from registry " + this.owner);
                return registryKey;
            }

            @Override
            void setRegistryKey(RegistryKey<ENTRY> registryKey) {
                if (this.registryKey != null && registryKey != this.registryKey)
                    throw new IllegalStateException("Can't change holder key: existing=" + this.registryKey + ", new=" + registryKey);
                this.registryKey = registryKey;
            }

            @Override
            void setValue(ENTRY value) {
                if (this.value != value)
                    throw new IllegalStateException("Can't change holder " + this.registryKey + " value: existing=" + this.value + ", new=" + value);
            }

            @Override
            public ENTRY value() {
                return value;
            }

            @Override
            public boolean hasKeyAndValue() {
                return registryKey != null && value != null;
            }
        }

        public static class IntrusiveReserved<ENTRY> extends Intrusive<ENTRY> {
            private final int reservedRawId;

            private IntrusiveReserved(RegistryEntryOwner<ENTRY> owner, ENTRY value, int reservedRawId) {
                super(owner, value);
                this.reservedRawId = reservedRawId;
            }

            public int reservedRawId() {
                return reservedRawId;
            }
        }
    }

    enum Type {
        REFERENCE,
        DIRECT
    }
}
