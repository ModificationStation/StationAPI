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
import net.modificationstation.stationapi.api.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    public <T> boolean resolve(
            ValueGetter<T> getter,
            Consumer<TagMatchGroup<T>> matchGroupConsumer
    ) {
        if (this.tag) {
            Collection<TagMatchGroup<T>> refTag = getter.tag(this.id);
            if (refTag == null) return !this.required;

            for (TagMatchGroup<T> refMatchGroup : refTag) {
                List<Condition<?>> mergedConditions = new ArrayList<>(refMatchGroup.conditions());

                if (!this.conditions.isEmpty()) mergedConditions.addAll(this.conditions);

                matchGroupConsumer.accept(new TagMatchGroup<>(refMatchGroup.baseItems(), mergedConditions, false));
            }

        } else {
            T value = getter.direct(this.id);
            if (value == null) return !this.required;

            matchGroupConsumer.accept(new TagMatchGroup<>(List.of(value), this.conditions, false));
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

        @Nullable Collection<TagMatchGroup<T>> tag(Identifier id);
    }
}

