package net.modificationstation.stationapi.api.tag;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.util.context.Condition;

import java.util.List;

public record TagFile(List<TagEntry> entries, boolean replace) {
    public static Codec<TagFile> createCodec(Codec<Condition<?>> tagConditionCodec) {
        return RecordCodecBuilder.create(
                instance -> instance.group(
                        TagEntry.createCodec(tagConditionCodec).listOf().fieldOf("values")
                                .forGetter(TagFile::entries),
                        Codec.BOOL.optionalFieldOf("replace", false)
                                .forGetter(TagFile::replace)
                ).apply(instance, TagFile::new)
        );
    }
}

