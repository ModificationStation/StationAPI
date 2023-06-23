package net.modificationstation.stationapi.api.event.registry;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.registry.Identifier;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
public class RegistryEntryRemovedEvent<T> extends Event {
    int rawId; Identifier id; T object;
}
