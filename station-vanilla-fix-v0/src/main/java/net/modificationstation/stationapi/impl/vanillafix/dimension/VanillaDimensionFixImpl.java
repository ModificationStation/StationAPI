package net.modificationstation.stationapi.impl.vanillafix.dimension;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.world.dimension.NetherDimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.dimension.SkylandsDimension;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.dimension.v1.DimensionType;
import net.modificationstation.stationapi.api.dimension.v1.event.registry.DimensionTypeRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.world.dimension.VanillaDimensions;

import java.lang.invoke.MethodHandles;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public final class VanillaDimensionFixImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerDimensions(DimensionTypeRegistryEvent event) {
        event.registerLogical(1, -1, VanillaDimensions.THE_NETHER, DimensionType.builder(NetherDimension::new).build());
        event.register(0, VanillaDimensions.OVERWORLD, DimensionType.builder(OverworldDimension::new).build());
        event.registerLogical(2, 1, VanillaDimensions.SKYLANDS, DimensionType.builder(SkylandsDimension::new).build());
    }
}
