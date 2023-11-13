package net.modificationstation.sltest.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.registry.DimensionContainer;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;

import static net.modificationstation.sltest.SLTest.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

public class DimensionListener {

    @EventListener
    private static void registerDimensions(DimensionRegistryEvent event) {
        DimensionRegistry r = event.registry;
        r.register(of(NAMESPACE, "test_dimension"), new DimensionContainer<>(TestDimension::new));
    }
}
