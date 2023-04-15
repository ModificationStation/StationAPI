package net.modificationstation.stationapi.api.resource;

import net.modificationstation.stationapi.api.util.profiler.ReadableProfiler;

import java.util.function.BiFunction;

public record ResourceReloaderProfilers(ReadableProfiler prepare, ReadableProfiler apply) {
    @FunctionalInterface
    public interface Factory {
        static <T> Factory of(BiFunction<ResourceReloader, T, ReadableProfiler> factory, T prepare, T apply) {
            return reloader -> new ResourceReloaderProfilers(factory.apply(reloader, prepare), factory.apply(reloader, apply));
        }

        ResourceReloaderProfilers of(ResourceReloader reloader);
    }
}
