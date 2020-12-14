package net.modificationstation.stationloader.api.common.event;

import net.modificationstation.stationloader.api.common.StationLoader;
import net.modificationstation.stationloader.api.common.registry.Identifier;
import net.modificationstation.stationloader.api.common.registry.Registry;

public final class EventRegistry extends Registry<Event<?>> {

    public static final EventRegistry INSTANCE = new EventRegistry(Identifier.of(StationLoader.INSTANCE.getModID(), "events"));

    private EventRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }
}
