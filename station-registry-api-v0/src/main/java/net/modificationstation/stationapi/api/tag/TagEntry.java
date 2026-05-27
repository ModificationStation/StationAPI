package net.modificationstation.stationapi.api.tag;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.context.Condition;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TagEntry {
    public static Codec<TagEntry> createCodec(Codec<Condition<?>> tagConditionCodec) {
        return Codec.either(
                Codecs.TAG_ENTRY_ID,
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
                either -> either.map(
                        id -> new TagEntry(id, true),
                        tagEntry -> tagEntry
                ),
                entry -> entry.required ? Either.left(entry.getIdForCodec()) : Either.right(entry)
        );
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

