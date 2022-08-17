package net.modificationstation.stationapi.api.util.dynamic;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.*;
import net.modificationstation.stationapi.api.registry.DynamicRegistryManager;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public interface EntryLoader {
    /**
     * @return A collection of file Identifiers of all known entries of the given registry.
     * Note that these are file Identifiers for use in a resource manager, not the logical names of the entries.
     */
    <E> Map<RegistryKey<E>, Parseable<E>> getKnownEntryPaths(RegistryKey<? extends Registry<E>> var1);

    <E> Optional<Parseable<E>> createParseable(RegistryKey<E> var1);

    static EntryLoader resourceBacked(final ResourceManager resourceManager) {
        return new EntryLoader(){
            private static final String JSON = ".json";

            @Override
            public <E> Map<RegistryKey<E>, Parseable<E>> getKnownEntryPaths(RegistryKey<? extends Registry<E>> key) {
                String string = getPath(key.getValue());
                Map<RegistryKey<E>, Parseable<E>> map = new HashMap<>();
                resourceManager.findResources(string, id -> id.id.endsWith(JSON)).forEach((id, resourceRef) -> {
                    String string2 = id.id;
                    String string3 = string2.substring(string.length() + 1, string2.length() - JSON.length());
                    RegistryKey<E> registryKey2 = RegistryKey.of(key, Identifier.of(id.modID, string3));
                    map.put(registryKey2, (jsonOps, decoder) -> {
                        try {
                            Reader reader = resourceRef.getReader();

                            DataResult<Entry<E>> var6;
                            try {
                                var6 = this.parse(jsonOps, decoder, reader);
                            } catch (Throwable var9) {
                                if (reader != null) {
                                    try {
                                        reader.close();
                                    } catch (Throwable var8) {
                                        var9.addSuppressed(var8);
                                    }
                                }

                                throw var9;
                            }

                            if (reader != null) {
                                reader.close();
                            }

                            return var6;
                        } catch (JsonIOException | JsonSyntaxException | IOException var10) {
                            return DataResult.error("Failed to parse " + id + " file: " + var10.getMessage());
                        }
                    });
                });
                return map;
            }

            @Override
            public <E> Optional<Parseable<E>> createParseable(RegistryKey<E> key) {
                Identifier identifier = createId(key);
                return resourceManager.getResource(identifier).map((resource) -> (jsonOps, decoder) -> {
                    try {
                        Reader reader = resource.getReader();

                        DataResult<Entry<E>> var6;
                        try {
                            var6 = this.parse(jsonOps, decoder, reader);
                        } catch (Throwable var9) {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (Throwable var8) {
                                    var9.addSuppressed(var8);
                                }
                            }

                            throw var9;
                        }

                        if (reader != null) {
                            reader.close();
                        }

                        return var6;
                    } catch (JsonIOException | JsonSyntaxException | IOException var10) {
                        return DataResult.error("Failed to parse " + identifier + " file: " + var10.getMessage());
                    }
                });
            }

            private <E> DataResult<Entry<E>> parse(DynamicOps<JsonElement> jsonOps, Decoder<E> decoder, Reader reader) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                return decoder.parse(jsonOps, jsonElement).map(Entry::of);
            }

            private static String getPath(Identifier id) {
                return id.id;
            }

            private static <E> Identifier createId(RegistryKey<E> rootKey) {
                return Identifier.of(rootKey.getValue().modID, getPath(rootKey.getRegistry()) + "/" + rootKey.getValue().id + JSON);
            }

            public String toString() {
                return "ResourceAccess[" + resourceManager + "]";
            }
        };
    }

    final class Impl implements EntryLoader {
        private final Map<RegistryKey<?>, Element> values = Maps.newIdentityHashMap();

        public <E> void add(DynamicRegistryManager registryManager, RegistryKey<E> key, Encoder<E> encoder, int rawId, E entry, Lifecycle lifecycle) {
            DataResult<JsonElement> dataResult = encoder.encodeStart(RegistryOps.of(JsonOps.INSTANCE, registryManager), entry);
            Optional<DataResult.PartialResult<JsonElement>> optional = dataResult.error();
            if (optional.isPresent()) {
                LOGGER.error("Error adding element: {}", optional.get().message());
            } else {
                this.values.put(key, new Element(dataResult.result().orElseThrow(), rawId, lifecycle));
            }
        }

        @Override
        public <E> Map<RegistryKey<E>, Parseable<E>> getKnownEntryPaths(RegistryKey<? extends Registry<E>> key) {
            //noinspection unchecked
            return this.values.entrySet().stream().filter(entry -> entry.getKey().isOf(key)).collect(Collectors.toMap(entry -> (RegistryKey<E>) entry.getKey(), entry -> entry.getValue()::parse));
        }

        @Override
        public <E> Optional<Parseable<E>> createParseable(RegistryKey<E> key) {
            Element element = this.values.get(key);
            if (element == null) {
                DataResult<Entry<E>> dataResult = DataResult.error("Unknown element: " + key);
                return Optional.of((jsonOps, decoder) -> dataResult);
            }
            return Optional.of(element::parse);
        }

        record Element(JsonElement data, int id, Lifecycle lifecycle) {
            public <E> DataResult<Entry<E>> parse(DynamicOps<JsonElement> jsonOps, Decoder<E> decoder) {
                return decoder.parse(jsonOps, this.data).setLifecycle(this.lifecycle).map(value -> Entry.of(value, this.id));
            }
        }
    }

    @FunctionalInterface
    interface Parseable<E> {
        DataResult<Entry<E>> parseElement(DynamicOps<JsonElement> var1, Decoder<E> var2);
    }

    record Entry<E>(E value, OptionalInt fixedId) {
        public static <E> Entry<E> of(E value) {
            return new Entry<E>(value, OptionalInt.empty());
        }

        public static <E> Entry<E> of(E value, int id) {
            return new Entry<E>(value, OptionalInt.of(id));
        }
    }
}

