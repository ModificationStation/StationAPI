package net.modificationstation.stationapi.api.celestial;

import com.mojang.serialization.Lifecycle;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * Automatically handles initialization of celestial events.
 * Ensures that all celestial events are loaded correctly.
 */
public class CelestialEventRegistry extends SimpleRegistry<CelestialEvent> {
    public static final Identifier IDENTIFIER = Identifier.of(StationAPI.NAMESPACE, "celestial_events");
    public static final RegistryKey<Registry<CelestialEvent>> KEY = RegistryKey.ofRegistry(IDENTIFIER);

    public static final CelestialEventRegistry INSTANCE = new CelestialEventRegistry();

    private CelestialEventRegistry() {
        super(KEY, Lifecycle.stable());
    }

    /**
     * Initializes all events when the world is loaded, ensures correct loading of active events.
     */
    public void initializeEvents(World world) {
        stream().forEach(celestialEvent -> {
            if (celestialEvent == null) return;
            celestialEvent.markForInitialization(world);
        });
    }
}
