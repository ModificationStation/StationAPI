//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.modificationstation.stationapi.api.tag;

import com.google.common.collect.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class TagGroupLoader<T> {
    private static final String JSON_EXTENSION = ".json";
    private static final int JSON_EXTENSION_LENGTH = JSON_EXTENSION.length();
    final Function<Identifier, Optional<? extends T>> registryGetter;
    private final String dataType;

    public TagGroupLoader(Function<Identifier, Optional<? extends T>> registryGetter, String dataType) {
        this.registryGetter = registryGetter;
        this.dataType = dataType;
    }

    public Map<Identifier, List<TrackedEntry>> loadTags(ResourceManager manager) {
        Reference2ReferenceMap<Identifier, List<TrackedEntry>> map = new Reference2ReferenceOpenHashMap<>();

        for (Map.Entry<Identifier, List<Resource>> entry : manager.findAllResources(this.dataType, id -> id.id.endsWith(JSON_EXTENSION)).entrySet()) {
            Identifier identifier = entry.getKey();
            String string = identifier.id;
            Identifier identifier2 = Identifier.of(identifier.modID, string.substring(this.dataType.length() + 1, string.length() - JSON_EXTENSION_LENGTH));

            for (Resource resource : entry.getValue())
                try {
                    Reader reader = resource.getReader();

                    try {
                        JsonElement jsonElement = JsonParser.parseReader(reader);
                        List<TrackedEntry> list = map.computeIfAbsent(identifier2, identifierx -> new ArrayList<>());
                        DataResult<TagFile> var10000 = TagFile.CODEC.parse(new Dynamic<>(JsonOps.INSTANCE, jsonElement));
                        Logger var10002 = LOGGER;
                        Objects.requireNonNull(var10002);
                        TagFile tagFile = var10000.getOrThrow(false, var10002::error);
                        if (tagFile.replace()) list.clear();

                        String string2 = resource.getResourcePackName();
                        tagFile.entries().forEach(tagEntry -> list.add(new TrackedEntry(tagEntry, string2)));
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
                    LOGGER.error("Couldn't read tag list {} from {}"/* in data pack {}"*/, new Object[]{identifier2, identifier/*, resource.getResourcePackName()*/, var17});
                }
        }

        return map;
    }

    private static void method_32839(Map<Identifier, List<TrackedEntry>> map, Multimap<Identifier, Identifier> multimap, Set<Identifier> set, Identifier identifier, BiConsumer<Identifier, List<TrackedEntry>> biConsumer) {
        if (set.add(identifier)) {
            multimap.get(identifier).forEach(identifierx -> method_32839(map, multimap, set, identifierx, biConsumer));
            List<TrackedEntry> list = map.get(identifier);
            if (list != null) biConsumer.accept(identifier, list);

        }
    }

    private static boolean method_32836(Multimap<Identifier, Identifier> multimap, Identifier identifier, Identifier identifier2) {
        Collection<Identifier> collection = multimap.get(identifier2);
        return collection.contains(identifier) || collection.stream().anyMatch(identifier2x -> method_32836(multimap, identifier, identifier2x));
    }

    private static void method_32844(Multimap<Identifier, Identifier> multimap, Identifier identifier, Identifier identifier2) {
        if (!method_32836(multimap, identifier, identifier2)) multimap.put(identifier, identifier2);

    }

    private Either<Collection<TrackedEntry>, Collection<T>> method_43952(TagEntry.ValueGetter<T> valueGetter, List<TrackedEntry> list) {
        ImmutableSet.Builder<T> builder = ImmutableSet.builder();
        List<TrackedEntry> list2 = new ArrayList<>();

        for (TrackedEntry trackedEntry : list) {
            TagEntry var10000 = trackedEntry.entry();
            Objects.requireNonNull(builder);
            if (!var10000.resolve(valueGetter, builder::add)) list2.add(trackedEntry);
        }

        return list2.isEmpty() ? Either.right(builder.build()) : Either.left(list2);
    }

    public Map<Identifier, Collection<T>> buildGroup(Map<Identifier, List<TrackedEntry>> map) {
        final Map<Identifier, Collection<T>> map2 = Maps.newHashMap();
        TagEntry.ValueGetter<T> valueGetter = new TagEntry.ValueGetter<>() {
            @Nullable
            public T direct(Identifier id) {
                return TagGroupLoader.this.registryGetter.apply(id).orElse(null);
            }

            @Nullable
            public Collection<T> tag(Identifier id) {
                return map2.get(id);
            }
        };
        Multimap<Identifier, Identifier> multimap = HashMultimap.create();
        map.forEach((identifier, list) -> list.forEach(trackedEntry -> trackedEntry.entry.forEachRequiredTagId(identifier2 -> method_32844(multimap, identifier, identifier2))));
        map.forEach((identifier, list) -> list.forEach(trackedEntry -> trackedEntry.entry.forEachOptionalTagId(identifier2 -> method_32844(multimap, identifier, identifier2))));
        Set<Identifier> set = Sets.newHashSet();
        map.keySet().forEach(identifier -> method_32839(map, multimap, set, identifier, (identifierx, list) -> this.method_43952(valueGetter, list).ifLeft(collection -> LOGGER.error("Couldn't load tag {} as it is missing following references: {}", identifierx, collection.stream().map(Objects::toString).collect(Collectors.joining(", ")))).ifRight(collection -> map2.put(identifierx, collection))));
        return map2;
    }

    public Map<Identifier, Collection<T>> load(ResourceManager manager) {
        return this.buildGroup(this.loadTags(manager));
    }

    public record TrackedEntry(TagEntry entry, String source) {

        @Override
        public String toString() {
            return this.entry.toString() + " (from " + this.source + ")";
        }
    }
}
