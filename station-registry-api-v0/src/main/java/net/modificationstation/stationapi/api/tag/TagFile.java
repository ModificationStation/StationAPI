package net.modificationstation.stationapi.api.tag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.util.context.Condition;
import net.modificationstation.stationapi.api.util.context.ConditionType;

import java.util.List;

public record TagFile(List<TagEntry> entries, List<TagEntry> remove, boolean replace) {
    public static Codec<TagFile> createCodec(Codec<Condition<?>> tagConditionCodec, Iterable<ConditionType<?>> tagConditionTypes) {
        return RecordCodecBuilder.create(
                instance -> instance.group(
                        TagEntry.createCodec(tagConditionCodec, tagConditionTypes).listOf().optionalFieldOf("values", List.of())
                                .forGetter(TagFile::entries),
                        TagEntry.createCodec(tagConditionCodec, tagConditionTypes).listOf().optionalFieldOf("remove", List.of())
                                .forGetter(TagFile::remove),
                        Codec.BOOL.optionalFieldOf("replace", false)
                                .forGetter(TagFile::replace)
                ).apply(instance, TagFile::new)
        );
    }
}

