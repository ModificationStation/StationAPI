package net.modificationstation.stationapi.api.tag;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TagEntry {
    private static final Codec<TagEntry> ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(Codecs.TAG_ENTRY_ID.fieldOf("id").forGetter(TagEntry::getIdForCodec), Codec.BOOL.optionalFieldOf("required", true).forGetter(entry -> entry.required)).apply(instance, TagEntry::new));
    public static final Codec<TagEntry> CODEC = Codec.either(Codecs.TAG_ENTRY_ID, ENTRY_CODEC).xmap(either -> either.map(id -> new TagEntry(id, true), tagEntry -> tagEntry), entry -> entry.required ? Either.left(entry.getIdForCodec()) : Either.right(entry));
    private final Identifier id;
    private final boolean tag;
    private final boolean required;

    private TagEntry(Identifier id, boolean tag, boolean required) {
        this.id = id;
        this.tag = tag;
        this.required = required;
    }

    private TagEntry(Codecs.TagEntryId id, boolean required) {
        this.id = id.id();
        this.tag = id.tag();
        this.required = required;
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

    public <T> boolean resolve(ValueGetter<T> valueGetter, Consumer<T> consumer) {
        if (this.tag) {
            Collection<T> collection = valueGetter.tag(this.id);
            if (collection == null) {
                return !this.required;
            }
            collection.forEach(consumer);
        } else {
            T object = valueGetter.direct(this.id);
            if (object == null) {
                return !this.required;
            }
            consumer.accept(object);
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
        @Nullable T direct(Identifier var1);

        @Nullable Collection<T> tag(Identifier var1);
    }
}

