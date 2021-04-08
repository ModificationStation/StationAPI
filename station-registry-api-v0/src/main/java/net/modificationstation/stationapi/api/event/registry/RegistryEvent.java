package net.modificationstation.stationapi.api.event.registry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.Registry;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RegistryEvent<T extends Registry<?>> extends Event {

    public final T registry;

}
