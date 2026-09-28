package net.modificationstation.stationapi.api.tag;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JavaOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.context.Condition;
import net.modificationstation.stationapi.api.util.context.ConditionType;
import net.modificationstation.stationapi.api.util.context.Context;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagEntry {
    public static Codec<TagEntry> createCodec(Codec<Condition<?>> tagConditionCodec, Iterable<ConditionType<?>> tagConditionTypes) {
        return Codec.either(
                Codec.STRING.comapFlatMap(
                        s -> {
                            String path = s;
                            List<Condition<?>> conditions = new ArrayList<>();
                            boolean matched;
                            do {
                                matched = false;
                                for (ConditionType<?> type : tagConditionTypes) {
                                    Optional<Pair<String, Condition<?>>> result = captureAndParse(type, path);
                                    if (result.isPresent()) {
                                        path = result.get().getFirst();
                                        conditions.add(result.get().getSecond());
                                        matched = true;
                                        break;
                                    }
                                }
                            } while (matched);
                            return Codecs.TAG_ENTRY_ID.parse(JavaOps.INSTANCE, path)
                                    .map(id -> new TagEntry(id, true, conditions));
                        },
                        entry -> entry.getIdForCodec().toString()
                ),
                RecordCodecBuilder.<TagEntry>create(
                        instance -> instance.group(
                                Codecs.TAG_ENTRY_ID.fieldOf("id")
                                        .forGetter(TagEntry::getIdForCodec),
                                Codec.BOOL.optionalFieldOf("required", true)
                                        .forGetter(entry -> entry.required),
                                tagConditionCodec.listOf().optionalFieldOf("conditions", List.of())
                                        .forGetter(entry -> entry.conditions)
                        ).apply(instance, TagEntry::new)
                )
        ).xmap(
                Either::unwrap,
                entry -> entry.required && entry.conditions.isEmpty()
                        ? Either.left(entry)
                        : Either.right(entry)
        );
    }

    private static <DATA> Optional<Pair<String, Condition<?>>> captureAndParse(ConditionType<DATA> type, String path) {
        Pattern pattern = type.shorthandPattern();
        if (pattern == null) return Optional.empty();

        Matcher matcher = pattern.matcher(path);
        if (!matcher.find()) return Optional.empty();

        String remaining = path.substring(0, matcher.start()) + path.substring(matcher.end());
        String extracted = matcher.group(1);

        Dynamic<?> dynamic = new Dynamic<>(JavaOps.INSTANCE, extracted);
        Dynamic<?> unfolded = type.unfolder().apply(dynamic);

        DataResult<DATA> result = type.dataCodec().codec().parse(unfolded);
        return result.result().map(data -> Pair.of(remaining, new Condition<>(type, data)));
    }

    private final Identifier id;
    private final boolean tag;
    private final boolean required;
    private final List<Condition<?>> conditions;

    private TagEntry(Identifier id, boolean tag, boolean required) {
        this.id = id;
        this.tag = tag;
        this.required = required;
        this.conditions = List.of();
    }

    private TagEntry(Codecs.TagEntryId id, boolean required) {
        this.id = id.id();
        tag = id.tag();
        this.required = required;
        conditions = List.of();
    }

    private TagEntry(Codecs.TagEntryId id, boolean required, List<Condition<?>> conditions) {
        this.id = id.id();
        tag = id.tag();
        this.required = required;
        this.conditions = List.copyOf(conditions);
    }

    private TagEntry(Identifier id, boolean tag, boolean required, List<Condition<?>> conditions) {
        this.id = id;
        this.tag = tag;
        this.required = required;
        this.conditions = List.copyOf(conditions);
    }

    /**
     * {@return this entry, or a copy of it with the given requiredness}
     *
     * <p>Used to make removals optional, so that removing something a data pack never added
     * doesn't fail the whole tag.
     */
    public TagEntry withRequired(boolean required) {
        return required == this.required ? this : new TagEntry(this.id, this.tag, required, this.conditions);
    }

    private Codecs.TagEntryId getIdForCodec() {
        return new Codecs.TagEntryId(this.id, this.tag);
    }

    public static TagEntry create(Identifier id) {
        return new TagEntry(id, false, true);
    }

    public static TagEntry createOptional(Identifier id) {
        return new TagEntry(id, false, false);
    }

    public static TagEntry createTag(Identifier id) {
        return new TagEntry(id, true, true);
    }

    public static TagEntry createOptionalTag(Identifier id) {
        return new TagEntry(id, true, false);
    }

    /**
     * Resolves this entry into the values it refers to, along with the context each value
     * is contributed in.
     *
     * <p>A tag reference resolves against the referenced tag's <em>final</em> membership, so an
     * entry the referenced tag removed is simply not there to be seen. The caller decides what to
     * do with each resolved value, which is what keeps {@code values} and {@code remove} symmetric:
     * they run the same resolution and differ only in the action they pass in.
     *
     * @param getter        lookup for direct values and for already resolved tags
     * @param valueConsumer receives each resolved value and the predicate guarding it
     * @return whether this entry resolved, or was optional
     */
    public <T> boolean resolve(
            ValueGetter<T> getter,
            BiConsumer<T, Predicate<Context>> valueConsumer
    ) {
        Predicate<Context> own = TagConditions.of(this.conditions);
        if (this.tag) {
            Map<T, Predicate<Context>> refTag = getter.tag(this.id);
            if (refTag == null) return !this.required;

            refTag.forEach((value, predicate) -> valueConsumer.accept(value, TagConditions.and(predicate, own)));
        } else {
            T value = getter.direct(this.id);
            if (value == null) return !this.required;

            valueConsumer.accept(value, own);
        }
        return true;
    }

    public void forEachRequiredTagId(Consumer<Identifier> consumer) {
        if (this.tag && this.required) {
            consumer.accept(this.id);
        }
    }

    public void forEachOptionalTagId(Consumer<Identifier> consumer) {
        if (this.tag && !this.required) {
            consumer.accept(this.id);
        }
    }

    public boolean canAdd(Predicate<Identifier> directEntryPredicate, Predicate<Identifier> tagEntryPredicate) {
        return !this.required || (this.tag ? tagEntryPredicate : directEntryPredicate).test(this.id);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.tag) {
            stringBuilder.append('#');
        }
        stringBuilder.append(this.id);
        if (!this.required) {
            stringBuilder.append('?');
        }
        return stringBuilder.toString();
    }

    public interface ValueGetter<T> {
        @Nullable T direct(Identifier id);

        /**
         * {@return the already resolved membership of the given tag, or {@code null} if it is
         * unknown}
         *
         * <p>Tags resolve in dependency order, so this returns the referenced tag's final
         * contents, with its own removals already applied.
         */
        @Nullable Map<T, Predicate<Context>> tag(Identifier id);
    }
}

