package net.modificationstation.stationapi.api.event.registry;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.util.Identifier;

@SuperBuilder
@FieldDefaults(level = AccessLevel.PUBLIC, makeFinal = true)
@EventPhases(StationAPI.INTERNAL_PHASE)
public class RegistryIdRemapEvent<T> extends Event {
    RemapState<T> state;

    public interface RemapState<T> {
        Int2IntMap getRawIdChangeMap();
        Identifier getIdFromOld(int oldRawId);
        Identifier getIdFromNew(int newRawId);
    }
}
