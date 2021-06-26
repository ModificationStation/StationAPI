package net.modificationstation.stationapi.impl.registry;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.minecraft.util.io.CompoundTag;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.level.LevelPropertiesEvent;
import net.modificationstation.stationapi.api.event.registry.PostRegistryRemapEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.mod.entrypoint.EventBusPolicy;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.LevelSerialRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

import static net.modificationstation.stationapi.api.StationAPI.MODID;

@Entrypoint(eventBus = @EventBusPolicy(registerInstance = false))
public class LevelRegistryRemapper {

    @EventListener(priority = ListenerPriority.HIGH)
    private static void loadProperties(LevelPropertiesEvent.LoadOnLevelInit event) {
        Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
        String lsr = Identifier.of(MODID, "level_serial_registries").toString();
        if (event.tag.containsKey(lsr)) {
            CompoundTag registriesTag = event.tag.getCompoundTag(lsr);
            registriesRegistry.forEach((identifier, registry) -> {
                String id = registry.id.toString();
                if (registry instanceof LevelSerialRegistry<?> && registriesTag.containsKey(id))
                    ((LevelSerialRegistry<?>) registry).load(registriesTag.getCompoundTag(id));
            });
        }
        StationAPI.EVENT_BUS.post(new PostRegistryRemapEvent());
    }

    @EventListener(priority = ListenerPriority.HIGH)
    private static void saveProperties(LevelPropertiesEvent.Save event) {
        Registry<Registry<?>> registriesRegistry = Registry.REGISTRIES;
        CompoundTag registriesTag = new CompoundTag();
        registriesRegistry.forEach((identifier, registry) -> {
            if (registry instanceof LevelSerialRegistry) {
                CompoundTag registryTag = new CompoundTag();
                ((LevelSerialRegistry<?>) registry).save(registryTag);
                registriesTag.put(identifier.toString(), registryTag);
            }
        });
        event.tag.put(Identifier.of(MODID, "level_serial_registries").toString(), registriesTag);
    }
}
