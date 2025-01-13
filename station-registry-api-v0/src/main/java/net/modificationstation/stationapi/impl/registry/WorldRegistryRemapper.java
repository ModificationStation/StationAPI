package net.modificationstation.stationapi.impl.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.Listener;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.world.WorldPropertiesEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.legacy.WorldLegacyRegistry;

import java.lang.invoke.MethodHandles;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class WorldRegistryRemapper {
    static {
        Listener.registerLookup(MethodHandles.lookup());
    }

    @EventListener
    private static void saveProperties(WorldPropertiesEvent.Save event) {
        NbtCompound registriesTag = new NbtCompound();
        WorldLegacyRegistry.saveAll(registriesTag);
        event.nbt.put(of(NAMESPACE, "level_serial_registries").toString(), registriesTag);
    }

    @EventListener
    private static void loadProperties(WorldPropertiesEvent.LoadOnWorldInit event) {
        String lsr = of(NAMESPACE, "level_serial_registries").toString();
        if (event.nbt.contains(lsr))
            WorldLegacyRegistry.loadAll(event.nbt.getCompound(lsr));
    }
}
