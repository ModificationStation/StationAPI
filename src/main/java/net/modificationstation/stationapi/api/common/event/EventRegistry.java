package net.modificationstation.stationapi.api.common.event;

import net.modificationstation.stationapi.api.common.registry.Identifier;
import net.modificationstation.stationapi.api.common.registry.Registry;
import net.modificationstation.stationapi.impl.common.StationAPI;

public final class EventRegistry extends Registry<Event<?>> {

    public static final EventRegistry INSTANCE = new EventRegistry(Identifier.of(StationAPI.MODID, "events"));

    private EventRegistry(Identifier identifier) {
        super(identifier);
    }

    @Override
    public int getRegistrySize() {
        return Integer.MAX_VALUE;
    }
}
