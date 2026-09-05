//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.modificationstation.stationapi.api.tag;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.context.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class TagGroupLoader<T> {
    private static final String JSON_EXTENSION = ".json";
    private static final int JSON_EXTENSION_LENGTH = JSON_EXTENSION.length();
    final Function<Identifier, Optional<? extends T>> registryGetter;
    private final String dataType;

    private final Codec<TagFile> tagFileCodec;

    public TagGroupLoader(Function<Identifier, Optional<? extends T>> registryGetter, String dataType, Codec<TagFile> tagFileCodec) {
        this.registryGetter = registryGetter;
        this.dataType = dataType;
        this.tagFileCodec = tagFileCodec;
    }

    public Map<Identifier, List<TrackedEntry>> loadTags(ResourceManager manager) {
        Reference2ReferenceMap<Identifier, List<TrackedEntry>> map = new Reference2ReferenceOpenHashMap<>();

        for (Map.Entry<Identifier, List<Resource>> entry : manager.findAllResources(this.dataType, id -> id.path.endsWith(JSON_EXTENSION)).entrySet()) {
            Identifier identifier = entry.getKey();
            String string = identifier.path;
            Identifier identifier2 = Identifier.of(identifier.namespace, string.substring(this.dataType.length() + 1, string.length() - JSON_EXTENSION_LENGTH));

            for (Resource resource : entry.getValue())
                try {
                    Reader reader = resource.getReader();

                    try {
                        JsonElement jsonElement = JsonParser.parseReader(reader);
                        List<TrackedEntry> list = map.computeIfAbsent(identifier2, identifierx -> new ArrayList<>());
                        TagFile tagFile = tagFileCodec.parse(new Dynamic<>(JsonOps.INSTANCE, jsonElement)).getOrThrow();
                        if (tagFile.replace()) list.clear();

                        String string2 = resource.getResourcePackName();
                        tagFile.entries().forEach(tagEntry -> list.add(new TrackedEntry(tagEntry, false, string2)));
                        // Removals are optional at resolve time, so removing something no mod provides
                        // doesn't drop the whole tag. Mirrors NeoForge's handling.
                        tagFile.remove().forEach(tagEntry -> list.add(new TrackedEntry(tagEntry.withRequired(false), true, string2)));
                    } catch (Throwable var16) {
                        if (reader != null) try {
                            reader.close();
                        } catch (Throwable var15) {
                            var16.addSuppressed(var15);
                        }

                        throw var16;
                    }

                    reader.close();
                } catch (Exception var17) {
                    LOGGER.error(
                            "Couldn't read tag list {} from {} in data pack {}",
                            identifier2, identifier, resource.getResourcePackName(), var17
                    );
                }
        }

        return map;
    }

    private static void resolveAll(Map<Identifier, List<TrackedEntry>> map, Multimap<Identifier, Identifier> multimap, Set<Identifier> set, Identifier identifier, BiConsumer<Identifier, List<TrackedEntry>> biConsumer) {
        if (set.add(identifier)) {
            multimap.get(identifier).forEach(identifierx -> resolveAll(map, multimap, set, identifierx, biConsumer));
            List<TrackedEntry> list = map.get(identifier);
            if (list != null) biConsumer.accept(identifier, list);

        }
    }

    private static boolean hasCircularDependency(Multimap<Identifier, Identifier> multimap, Identifier identifier, Identifier identifier2) {
        Collection<Identifier> collection = multimap.get(identifier2);
        return collection.contains(identifier) || collection.stream().anyMatch(identifier2x -> hasCircularDependency(multimap, identifier, identifier2x));
    }

    private static void addReference(Multimap<Identifier, Identifier> multimap, Identifier identifier, Identifier identifier2) {
        if (!hasCircularDependency(multimap, identifier, identifier2)) multimap.put(identifier, identifier2);
    }

    /**
     * Folds a tag's entries into its final membership, applying each entry as an action in file
     * order: {@code values} entries add, {@code remove} entries subtract.
     *
     * <p>Doing this here rather than at registry population time is what makes a tag reference
     * see the referenced tag's final contents. A removal cannot be carried along as a flag on
     * resolved data, because whether a referenced value should end up added or removed depends on
     * what happened to it later in the referenced tag, which the individual entry cannot know.
     */
    private Either<Collection<TrackedEntry>, Map<T, Predicate<Context>>> resolveAll(TagEntry.ValueGetter<T> valueGetter, List<TrackedEntry> tags) {
        Map<T, Predicate<Context>> membership = new LinkedHashMap<>();
        List<TrackedEntry> missing = new ArrayList<>();

        for (TrackedEntry tag : tags) {
            BiConsumer<T, Predicate<Context>> action = tag.remove()
                    ? (value, predicate) -> TagConditions.remove(membership, value, predicate)
                    : (value, predicate) -> TagConditions.add(membership, value, predicate);
            if (!tag.entry().resolve(valueGetter, action)) missing.add(tag);
        }

        return missing.isEmpty()
                ? Either.right(membership)
                : Either.left(missing);
    }

    public Map<Identifier, Map<T, Predicate<Context>>> buildGroup(Map<Identifier, List<TrackedEntry>> map) {
        final Map<Identifier, Map<T, Predicate<Context>>> map2 = Maps.newHashMap();
        TagEntry.ValueGetter<T> valueGetter = new TagEntry.ValueGetter<>() {
            @Nullable
            public T direct(Identifier id) {
                return TagGroupLoader.this.registryGetter.apply(id).orElse(null);
            }

            @Nullable
            public Map<T, Predicate<Context>> tag(Identifier id) {
                return map2.get(id);
            }
        };
        Multimap<Identifier, Identifier> multimap = HashMultimap.create();
        map.forEach(
                (identifier, list) -> list.forEach(
                        trackedEntry -> trackedEntry.entry.forEachRequiredTagId(
                                identifier2 -> addReference(multimap, identifier, identifier2
                                )
                        )
                )
        );
        map.forEach(
                (identifier, list) -> list.forEach(
                        trackedEntry -> trackedEntry.entry.forEachOptionalTagId(
                                identifier2 -> addReference(multimap, identifier, identifier2)
                        )
                )
        );
        Set<Identifier> set = Sets.newHashSet();
        map.keySet().forEach(identifier -> resolveAll(
                map, multimap, set, identifier, (identifierx, list) -> this.resolveAll(
                        valueGetter, list
                ).ifLeft(
                        collection -> LOGGER.error(
                                "Couldn't load tag {} as it is missing following references: {}",
                                identifierx,
                                collection.stream()
                                        .map(Objects::toString)
                                        .collect(Collectors.joining(", "))
                        )
                ).ifRight(
                        collection -> map2.put(identifierx, collection)
                )
        ));
        return map2;
    }

    public Map<Identifier, Map<T, Predicate<Context>>> load(ResourceManager manager) {
        return this.buildGroup(this.loadTags(manager));
    }

    public record TrackedEntry(TagEntry entry, boolean remove, String source) {
        @Override
        public @NotNull String toString() {
            return this.entry.toString() + " (from " + this.source + ")";
        }
    }
}
