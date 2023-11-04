package net.modificationstation.stationapi.impl.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.nbt.NbtCompound;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelPropertiesEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.legacy.LevelLegacyRegistry;

import static net.modificationstation.stationapi.api.StationAPI.NAMESPACE;
import static net.modificationstation.stationapi.api.util.Identifier.of;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
@EventListener(phase = StationAPI.INTERNAL_PHASE)
public class LevelRegistryRemapper {

    @EventListener
    private static void saveProperties(LevelPropertiesEvent.Save event) {
        NbtCompound registriesTag = new NbtCompound();
        LevelLegacyRegistry.saveAll(registriesTag);
        event.tag.put(of(NAMESPACE, "level_serial_registries").toString(), registriesTag);
    }

    @EventListener
    private static void loadProperties(LevelPropertiesEvent.LoadOnLevelInit event) {
        String lsr = of(NAMESPACE, "level_serial_registries").toString();
        if (event.tag.contains(lsr))
            LevelLegacyRegistry.loadAll(event.tag.getCompound(lsr));
    }
}
