package net.modificationstation.stationapi.api.event.resource;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.resource.DataManager;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class DataResourceReloaderRegisterEvent extends Event {
    public final DataManager resourceManager;
}
