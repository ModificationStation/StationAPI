package net.modificationstation.stationapi.api.resource;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.Function;

public final class FakeResourceManager {

    private static final ReferenceSet<Function<String, InputStream>> FAKE_RESOURCE_PROVIDERS = new ReferenceOpenHashSet<>();

    public static void registerProvider(Function<String, InputStream> provider) {
        FAKE_RESOURCE_PROVIDERS.add(provider);
    }

    public static InputStream get(String path) {
        return FAKE_RESOURCE_PROVIDERS.stream().map(provider -> provider.apply(path)).filter(Objects::nonNull).findFirst().orElse(null);
    }
}
