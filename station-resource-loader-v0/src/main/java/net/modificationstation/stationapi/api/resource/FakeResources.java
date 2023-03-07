package net.modificationstation.stationapi.api.resource;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;

import java.util.Optional;
import java.util.function.Function;

public final class FakeResources {

    private static final ReferenceSet<Function<String, Optional<Resource>>> FAKE_FACTORIES = new ReferenceOpenHashSet<>();

    public static void registerProvider(Function<String, Optional<Resource>> fakeFactory) {
        FAKE_FACTORIES.add(fakeFactory);
    }

    public static Optional<Resource> get(String path) {
        for (Function<String, Optional<Resource>> fakeFactory : FAKE_FACTORIES) {
            Optional<Resource> resource = fakeFactory.apply(path);
            if (resource.isPresent())
                return resource;
        }
        return Optional.empty();
    }
}
