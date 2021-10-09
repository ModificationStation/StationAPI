package net.modificationstation.stationapi.impl.level.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.level.dimension.Nether;
import net.minecraft.level.dimension.Overworld;
import net.minecraft.level.dimension.Skylands;
import net.modificationstation.stationapi.api.event.registry.DimensionRegistryEvent;
import net.modificationstation.stationapi.api.level.dimension.DimensionContainer;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.DimensionRegistry;

import static net.modificationstation.stationapi.api.level.dimension.VanillaDimensions.OVERWORLD;
import static net.modificationstation.stationapi.api.level.dimension.VanillaDimensions.SKYLANDS;
import static net.modificationstation.stationapi.api.level.dimension.VanillaDimensions.THE_NETHER;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class DimensionRegistryInit {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void registerDimensions(DimensionRegistryEvent event) {
        DimensionRegistry r = event.registry;
        r.register(THE_NETHER, -1, new DimensionContainer<>(Nether::new));
        r.register(OVERWORLD, 0, new DimensionContainer<>(Overworld::new));
        r.register(SKYLANDS, 1, new DimensionContainer<>(Skylands::new));
    }
}
