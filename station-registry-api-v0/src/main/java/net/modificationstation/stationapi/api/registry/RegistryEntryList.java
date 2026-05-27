package net.modificationstation.stationapi.api.registry;

import com.mojang.datafixers.util.Either;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.tag.TagMatchGroup;
import net.modificationstation.stationapi.api.util.Util;
import net.modificationstation.stationapi.api.util.context.Condition;
import net.modificationstation.stationapi.api.util.context.Context;
import net.modificationstation.stationapi.api.util.context.TagEvaluationContext;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A registry entry list is an immutable list of registry entries. This is either a direct
 * reference to each item, or a reference to a tag. A <strong>tag</strong> is a way
 * to dynamically define a list of registered values. Anything registered in a registry
 * can be tagged, and each registry holds a list of tags it recognizes.
 * 
 * <p>This can be iterated directly (i.e. {@code for (RegistryEntry<T> entry : entries)}).
 * Note that this does not implement {@link java.util.Collection}.
 * 
 * @see Registry
 * @see RegistryEntry
 */
public interface RegistryEntryList<T> extends Iterable<RegistryEntry<T>> {
    /**
     * This method implicitly uses {@link TagEvaluationContext#BYPASSED}, meaning it will stream all registry entries
     * in this list, bypassing all conditional checks.
     * {@return a stream of registry entries in this list}
     */
    default Stream<RegistryEntry<T>> stream() {
        return stream(TagEvaluationContext.BYPASSED);
    }

    /**
     * {@return a stream of registry entries in this list, including match groups that pass the given context}
     */
    Stream<RegistryEntry<T>> stream(Context context);

    /**
     * {@return an iterable of registry entries in this list, including match groups that pass the given context}
     */
    default Iterable<RegistryEntry<T>> iterable(Context context) {
        return () -> stream(context).iterator();
    }

    /**
     * This method implicitly uses {@link TagEvaluationContext#BYPASSED}, meaning it will count all registry entries
     * in this list, bypassing all conditional checks.
     * {@return the number of entries in this list}
     */
    default int size() {
        return size(TagEvaluationContext.BYPASSED);
    }

    /**
     * {@return the number of entries in this list, including match groups that pass the given context}
     */
    int size(Context context);

    /**
     * {@return the object that identifies this registry entry list}
     * 
     * <p>This is the tag key for a reference list, and the backing list for a direct list.
     */
    Either<TagKey<T>, List<RegistryEntry<T>>> getStorage();

    /**
     * This method implicitly uses {@link TagEvaluationContext#BYPASSED}, meaning it may return any registry entry
     * in this list, bypassing all conditional checks.
     * {@return a random entry of the list, or an empty optional if this list is empty}
     */
    default Optional<RegistryEntry<T>> getRandom(Random var1) {
        return getRandom(var1, TagEvaluationContext.BYPASSED);
    }

    /**
     * {@return a random entry of the list matching the context, or an empty optional if this list is empty}
     */
    Optional<RegistryEntry<T>> getRandom(Random var1, Context context);

    /**
     * This method implicitly uses {@link TagEvaluationContext#BYPASSED}, meaning it operates on the flattened list of all registry entries
     * in this list, bypassing all conditional checks.
     * {@return the registry entry at {@code index}}
     * 
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    default RegistryEntry<T> get(int var1) {
        return get(var1, TagEvaluationContext.BYPASSED);
    }

    /**
     * {@return the registry entry at {@code index} matching the context}
     * 
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    RegistryEntry<T> get(int var1, Context context);

    /**
     * {@return whether {@code entry} is in this list}
     *
     * @deprecated Use {@link #contains(RegistryEntry, Context)} instead.
     * <p>This method implicitly uses {@link Context#EMPTY}, meaning it will only evaluate to {@code true}
     * for unconditional tag references.
     */
    @Deprecated
    default boolean contains(RegistryEntry<T> entry) {
        return contains(entry, Context.EMPTY);
    }

    /**
     * {@return whether {@code entry} is in this list, evaluating match groups with the given context}
     */
    boolean contains(RegistryEntry<T> var1, Context context);

    boolean ownerEquals(RegistryEntryOwner<T> var1);

    Optional<TagKey<T>> getTagKey();

    @Deprecated
    @VisibleForTesting
    static <T> Named<T> of(RegistryEntryOwner<T> owner, TagKey<T> tagKey) {
        return new Named<>(owner, tagKey);
    }

    /**
     * {@return a new direct list of {@code entries}}
     */
    @SafeVarargs
    static <T> Direct<T> of(RegistryEntry<T> ... entries) {
        return new Direct<>(List.of(entries));
    }

    /**
     * {@return a new direct list of {@code entries}}
     */
    static <T> Direct<T> of(List<? extends RegistryEntry<T>> entries) {
        return new Direct<>(List.copyOf(entries));
    }

    /**
     * {@return a new direct list of {@code values} converted to a registry entry with {@code mapper}}
     */
    @SafeVarargs
    static <E, T> Direct<T> of(Function<E, RegistryEntry<T>> mapper, E ... values) {
        return RegistryEntryList.of(Stream.of(values).map(mapper).toList());
    }

    /**
     * {@return a new direct list of {@code values} converted to a registry entry with {@code mapper}}
     */
    static <E, T> Direct<T> of(Function<E, RegistryEntry<T>> mapper, List<E> values) {
        return RegistryEntryList.of(values.stream().map(mapper).toList());
    }

    class Named<T> extends ListBacked<T> {
        private final RegistryEntryOwner<T> owner;
        private final TagKey<T> tag;
        private List<RegistryEntry<T>> allEntries = List.of();
        private List<TagMatchGroup<RegistryEntry<T>>> matchGroups = List.of();

        Named(RegistryEntryOwner<T> owner, TagKey<T> tag) {
            this.owner = owner;
            this.tag = tag;
        }

        void copyOf(Collection<TagMatchGroup<RegistryEntry<T>>> matchGroups) {
            this.matchGroups = List.copyOf(matchGroups);
            this.allEntries = this.matchGroups.stream().flatMap(c -> c.baseItems().stream()).distinct().toList();
        }

        public TagKey<T> getTag() {
            return this.tag;
        }

        @Override
        protected List<RegistryEntry<T>> getEntries() {
            return this.allEntries;
        }

        @Override
        public Stream<RegistryEntry<T>> stream(Context context) {
            return Boolean.TRUE.equals(context.get(TagEvaluationContext.IGNORE_TAG_CONDITIONS))
                    ? this.getEntries().stream()
                    : this.matchGroups.stream()
                    .filter(matchGroup -> {
                        for (Condition<?> condition : matchGroup.conditions())
                            if (!condition.test(context)) return false;
                        return true;
                    })
                    .flatMap(matchGroup -> matchGroup.baseItems().stream())
                    .distinct();
        }

        @Override
        public int size(Context context) {
            return Boolean.TRUE.equals(context.get(TagEvaluationContext.IGNORE_TAG_CONDITIONS)) ? this.allEntries.size() : (int) this.stream(context).count();
        }

        @Override
        public Either<TagKey<T>, List<RegistryEntry<T>>> getStorage() {
            return Either.left(this.tag);
        }

        @Override
        public Optional<TagKey<T>> getTagKey() {
            return Optional.of(this.tag);
        }

        @Override
        public boolean contains(RegistryEntry<T> entry, Context context) {
            return entry.isIn(this.tag, context);
        }

        public String toString() {
            return "NamedSet(" + this.tag + ")["
                    + this.allEntries + (matchGroups.isEmpty() ? "" : ", matchGroups=" + matchGroups.size())
                    + "]";
        }

        @Override
        public boolean ownerEquals(RegistryEntryOwner<T> owner) {
            return this.owner.ownerEquals(owner);
        }
    }

    class Direct<T> extends ListBacked<T> {
        private final List<RegistryEntry<T>> entries;
        @Nullable
        private Set<RegistryEntry<T>> entrySet;

        Direct(List<RegistryEntry<T>> entries) {
            this.entries = entries;
        }

        @Override
        protected List<RegistryEntry<T>> getEntries() {
            return this.entries;
        }

        @Override
        public Either<TagKey<T>, List<RegistryEntry<T>>> getStorage() {
            return Either.right(this.entries);
        }

        @Override
        public Optional<TagKey<T>> getTagKey() {
            return Optional.empty();
        }

        @Override
        public boolean contains(RegistryEntry<T> entry, Context context) {
            if (this.entrySet == null) this.entrySet = Set.copyOf(this.entries);
            return this.entrySet.contains(entry);
        }

        public String toString() {
            return "DirectSet[" + this.entries + "]";
        }
    }

    abstract class ListBacked<T> implements RegistryEntryList<T> {
        protected abstract List<RegistryEntry<T>> getEntries();

        @Override
        public int size(Context context) {
            return this.getEntries().size();
        }

        @Override
        public Spliterator<RegistryEntry<T>> spliterator() {
            return this.stream(TagEvaluationContext.BYPASSED).spliterator();
        }

        @Override
        public Iterator<RegistryEntry<T>> iterator() {
            return this.stream(TagEvaluationContext.BYPASSED).iterator();
        }

        @Override
        public Stream<RegistryEntry<T>> stream(Context context) {
            return this.getEntries().stream();
        }

        @Override
        public Optional<RegistryEntry<T>> getRandom(Random random, Context context) {
            return Util.getRandomOrEmpty(this.stream(context).toList(), random);
        }

        @Override
        public RegistryEntry<T> get(int index, Context context) {
            return this.stream(context).skip(index).findFirst()
                    .orElseThrow(() -> new IndexOutOfBoundsException("Index out of bounds: " + index));
        }

        @Override
        public boolean ownerEquals(RegistryEntryOwner<T> owner) {
            return true;
        }
    }
}
