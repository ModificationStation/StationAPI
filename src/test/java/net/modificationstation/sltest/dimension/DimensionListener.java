package net.modificationstation.sltest.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;
import net.modificationstation.stationapi.api.dimension.v1.event.registry.DimensionTypeRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;

import java.lang.invoke.MethodHandles;

import static net.modificationstation.sltest.SLTest.NAMESPACE;

public class DimensionListener {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerDimensions(DimensionTypeRegistryEvent event) {
        event.register(NAMESPACE.id("test_dimension"), DimensionType.builder(type -> new TestDimension()).build());
    }
}
