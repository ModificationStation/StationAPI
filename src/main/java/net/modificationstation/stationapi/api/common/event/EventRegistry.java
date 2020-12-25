package net.modificationstation.stationapi.api.common.event;

import net.modificationstation.stationapi.api.common.StationAPI;
import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.Registry;

public final class EventRegistry extends Registry<Event<?>> {

    public static final EventRegistry INSTANCE = new EventRegistry(Identifier.of(StationAPI.INSTANCE.getModID(), "events"));

    private EventRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }
}
