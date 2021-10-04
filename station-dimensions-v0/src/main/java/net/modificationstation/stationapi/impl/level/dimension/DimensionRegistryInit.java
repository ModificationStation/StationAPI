package net.modificationstation.stationapi.impl.level.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.dimension.Overworld;
import net.minecraft.level.dimension.Skylands;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;

import static net.modificationstation.stationapi.api.registry.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class DimensionRegistryInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerDimensions(DimensionRegistryEvent event) {
        DimensionRegistry r = event.registry;
        r.register(of("the_nether"), -1, Nether::new);
        r.register(of("overworld"), 0, Overworld::new);
        r.register(of("skylands"), 1, Skylands::new);
    }
}
