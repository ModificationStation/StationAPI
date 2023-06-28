package net.modificationstation.stationapi.api.event.registry;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.registry.Identifier;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@EventPhases(StationAPI.INTERNAL_PHASE)
public class RegistryEntryAddedEvent<T> extends Event {
    int rawId; Identifier id; T object;
}
