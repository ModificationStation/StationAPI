package net.modificationstation.stationapi.api.resource.metadata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class ResourceFilter {

    private static final Codec<ResourceFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group((Codec.list(BlockEntry.CODEC).fieldOf("block")).forGetter(filter -> filter.blocks)).apply(instance, ResourceFilter::new));
    public static final ResourceMetadataSerializer<ResourceFilter> SERIALIZER = ResourceMetadataSerializer.fromCodec("filter", CODEC);
    /**
     * The list of block rules, named {@code block} in the JSON format.
     */
    private final List<BlockEntry> blocks;

    public ResourceFilter(List<BlockEntry> blocks) {
        this.blocks = List.copyOf(blocks);
    }

    public boolean isNamespaceBlocked(String namespace) {
        return this.blocks.stream().anyMatch(block -> block.getNamespacePredicate().test(namespace));
    }

    public boolean isPathBlocked(String namespace) {
        return this.blocks.stream().anyMatch(block -> block.getPathPredicate().test(namespace));
    }
}

