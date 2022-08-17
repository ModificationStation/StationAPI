package net.modificationstation.stationapi.api.resource.metadata;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class ResourceFilter {
    static final Codec<ResourceFilter> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.list(BlockEntry.CODEC).fieldOf("block").forGetter(filter -> filter.blocks)).apply(instance, ResourceFilter::new));
    public static final ResourceMetadataReader<ResourceFilter> READER = new ResourceMetadataReader<>() {

        @Override
        public String getKey() {
            return "filter";
        }

        @Override
        public ResourceFilter fromJson(JsonObject jsonObject) {
            return CODEC.parse(JsonOps.INSTANCE, jsonObject).getOrThrow(false, LOGGER::error);
        }
    };
    /**
     * The list of block rules, named {@code block} in the JSON format.
     */
    private final List<BlockEntry> blocks;

    public ResourceFilter(List<BlockEntry> blocks) {
        this.blocks = List.copyOf(blocks);
    }

    public boolean isNamespaceBlocked(String namespace) {
        return this.blocks.stream().anyMatch(block -> block.namespacePredicate.test(namespace));
    }

    public boolean isPathBlocked(String namespace) {
        return this.blocks.stream().anyMatch(block -> block.pathPredicate.test(namespace));
    }

    static class BlockEntry
    implements Predicate<Identifier> {
        static final Codec<BlockEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codecs.REGULAR_EXPRESSION.optionalFieldOf("namespace").forGetter(entry -> entry.namespace), Codecs.REGULAR_EXPRESSION.optionalFieldOf("path").forGetter(entry -> entry.path)).apply(instance, BlockEntry::new));
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private final Optional<Pattern> namespace;
        final Predicate<String> namespacePredicate;
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private final Optional<Pattern> path;
        final Predicate<String> pathPredicate;

        private BlockEntry(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Pattern> namespace, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Pattern> path) {
            this.namespace = namespace;
            this.namespacePredicate = namespace.map(Pattern::asPredicate).orElse(namespace_ -> true);
            this.path = path;
            this.pathPredicate = path.map(Pattern::asPredicate).orElse(path_ -> true);
        }

        @Override
        public boolean test(Identifier identifier) {
            return this.namespacePredicate.test(identifier.modID.toString()) && this.pathPredicate.test(identifier.id);
        }
    }
}

