package net.modificationstation.stationapi.api.common.event.registry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.modificationstation.stationapi.api.common.event.Event;
import net.modificationstation.stationapi.api.common.registry.Registry;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RegistryEvent<T extends Registry<?>> extends Event {

    public final T registry;
}
