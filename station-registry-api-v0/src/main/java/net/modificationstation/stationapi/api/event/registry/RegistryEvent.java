package net.modificationstation.stationapi.api.event.registry;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.Registry;

/**
 * A superclass for events involving registries.
 *
 * @param <T> the type of the registry involved in this event.
 * @author mine_diver
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RegistryEvent<T extends Registry<?>> extends Event {

    /**
     * The instance of the event's registry.
     */
    public final T registry;
}
