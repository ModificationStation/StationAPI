package net.modificationstation.stationapi.api.registry;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import net.modificationstation.stationapi.api.resource.Resource;
import net.modificationstation.stationapi.api.resource.ResourceFinder;
import net.modificationstation.stationapi.api.resource.ResourceManager;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

import static net.modificationstation.stationapi.api.StationAPI.LOGGER;

public class RegistryLoader {

    public static DynamicRegistryManager.Immutable load(ResourceManager resourceManager, DynamicRegistryManager baseRegistryManager, List<Entry<?>> entries) {
        Map<RegistryKey<?>, Exception> map = new HashMap<>();
        List<Pair<MutableRegistry<?>, RegistryLoadable>> list = entries.stream().map(entry -> entry.getLoader(Lifecycle.stable(), map)).toList();
        RegistryOps.RegistryInfoGetter registryInfoGetter = RegistryLoader.createInfoGetter(baseRegistryManager, list);
        list.forEach(loader -> loader.getSecond().load(resourceManager, registryInfoGetter));
        list.forEach(loader -> {
            Registry<?> registry = loader.getFirst();
            try {
                registry.freeze();
            } catch (Exception exception) {
                map.put(registry.getKey(), exception);
            }
        });
        if (!map.isEmpty()) {
            RegistryLoader.writeLoadingError(map);
            throw new IllegalStateException("Failed to load registries due to above errors");
        }
        return new DynamicRegistryManager.ImmutableImpl(list.stream().map(Pair::getFirst).toList()).toImmutable();
    }

    private static RegistryOps.RegistryInfoGetter createInfoGetter(DynamicRegistryManager baseRegistryManager, List<Pair<MutableRegistry<?>, RegistryLoadable>> additionalRegistries) {
        final Map<RegistryKey<?>, RegistryOps.RegistryInfo<?>> map = new HashMap<>();
        baseRegistryManager.streamAllRegistries().forEach(entry -> map.put(entry.key(), RegistryLoader.createInfo(entry.value())));
        additionalRegistries.forEach(pair -> map.put(pair.getFirst().getKey(), RegistryLoader.createInfo(pair.getFirst())));
        return new RegistryOps.RegistryInfoGetter(){

            @Override
            public <T> Optional<RegistryOps.RegistryInfo<T>> getRegistryInfo(RegistryKey<? extends Registry<? extends T>> registryRef) {
                //noinspection unchecked
                return Optional.ofNullable((RegistryOps.RegistryInfo<T>) map.get(registryRef));
            }
        };
    }

    private static <T> RegistryOps.RegistryInfo<T> createInfo(MutableRegistry<T> registry) {
        return new RegistryOps.RegistryInfo<>(registry.getReadOnlyWrapper(), registry.createMutableEntryLookup(), registry.getLifecycle());
    }

    private static <T> RegistryOps.RegistryInfo<T> createInfo(Registry<T> registry) {
        return new RegistryOps.RegistryInfo<>(registry.getReadOnlyWrapper(), registry.getTagCreatingWrapper(), registry.getLifecycle());
    }

    private static void writeLoadingError(Map<RegistryKey<?>, Exception> exceptions) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Map<Identifier, Map<Identifier, Exception>> map = exceptions.entrySet().stream().collect(Collectors.groupingBy(entry -> entry.getKey().getRegistry(), Collectors.toMap(entry -> entry.getKey().getValue(), Map.Entry::getValue)));
        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            printWriter.printf("> Errors in registry %s:%n", entry.getKey());
            entry.getValue().entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(elementEntry -> {
                printWriter.printf(">> Errors in element %s:%n", elementEntry.getKey());
                elementEntry.getValue().printStackTrace(printWriter);
            });
        });
        printWriter.flush();
        LOGGER.error("Registry loading errors:\n{}", stringWriter);
    }

    private static String getPath(Identifier id) {
        return id.id;
    }

    static <E> void load(RegistryOps.RegistryInfoGetter registryInfoGetter, ResourceManager resourceManager, RegistryKey<? extends Registry<E>> registryRef, MutableRegistry<E> newRegistry, Decoder<E> decoder, Map<RegistryKey<?>, Exception> exceptions) {
        String string = RegistryLoader.getPath(registryRef.getValue());
        ResourceFinder resourceFinder = ResourceFinder.json(string);
        RegistryOps<JsonElement> registryOps = RegistryOps.of(JsonOps.INSTANCE, registryInfoGetter);
        for (Map.Entry<Identifier, Resource> entry : resourceFinder.findResources(resourceManager).entrySet()) {
            Identifier identifier = entry.getKey();
            RegistryKey<E> registryKey = RegistryKey.of(registryRef, resourceFinder.toResourceId(identifier));
            Resource resource = entry.getValue();
            try (BufferedReader reader = resource.getReader()) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                DataResult<E> dataResult = decoder.parse(registryOps, jsonElement);
                E object = dataResult.getOrThrow(false, error -> {});
                newRegistry.add(registryKey, object, resource.isAlwaysStable() ? Lifecycle.stable() : dataResult.lifecycle());
            } catch (Exception exception) {
                exceptions.put(registryKey, new IllegalStateException(String.format(Locale.ROOT, "Failed to parse %s from pack %s", identifier, resource.getResourcePackName()), exception));
            }
        }
    }

    interface RegistryLoadable {
        void load(ResourceManager var1, RegistryOps.RegistryInfoGetter var2);
    }

    public record Entry<T>(RegistryKey<? extends Registry<T>> key, Codec<T> elementCodec) {
        Pair<MutableRegistry<?>, RegistryLoadable> getLoader(Lifecycle lifecycle, Map<RegistryKey<?>, Exception> exceptions) {
            SimpleRegistry<T> mutableRegistry = new SimpleRegistry<>(this.key, lifecycle);
            RegistryLoadable registryLoadable = (resourceManager, registryInfoGetter) -> RegistryLoader.load(registryInfoGetter, resourceManager, this.key, mutableRegistry, this.elementCodec, exceptions);
            return Pair.of(mutableRegistry, registryLoadable);
        }
    }
}

