package net.modificationstation.stationapi.impl.tag.conditional;

import com.mojang.serialization.Codec;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;

import java.lang.invoke.MethodHandles;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class BlockTagConditionsImpl {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void registerConditions(BlockRegistryEvent event) {
        event.registry.buildBlockTagCondition(
                NAMESPACE.id("block_metadata"),
                Codec.INT.fieldOf("metadata"),
                (metadata, ctx) -> ctx.hasBlockMeta() && ctx.blockMeta() == metadata
        ).register();
    }
}
