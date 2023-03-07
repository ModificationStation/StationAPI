package net.modificationstation.stationapi.api.resource.metadata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.dynamic.Codecs;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class BlockEntry {
    public static final Codec<BlockEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codecs.REGULAR_EXPRESSION.optionalFieldOf("namespace").forGetter(entry -> entry.namespace), Codecs.REGULAR_EXPRESSION.optionalFieldOf("path").forGetter(entry -> entry.path)).apply(instance, BlockEntry::new));
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Pattern> namespace;
    private final Predicate<String> namespacePredicate;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Pattern> path;
    private final Predicate<String> pathPredicate;
    private final Predicate<Identifier> identifierPredicate;

    private BlockEntry(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Pattern> namespace, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Pattern> path) {
        this.namespace = namespace;
        this.namespacePredicate = namespace.map(Pattern::asPredicate).orElse(namespace_ -> true);
        this.path = path;
        this.pathPredicate = path.map(Pattern::asPredicate).orElse(path_ -> true);
        this.identifierPredicate = id -> this.namespacePredicate.test(id.modID.toString()) && this.pathPredicate.test(id.id);
    }

    public Predicate<String> getNamespacePredicate() {
        return this.namespacePredicate;
    }

    public Predicate<String> getPathPredicate() {
        return this.pathPredicate;
    }

    public Predicate<Identifier> getIdentifierPredicate() {
        return this.identifierPredicate;
    }
}

