package net.modificationstation.stationapi.impl.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.item.ItemStackEvent;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class ShearsOverrideEvent extends ItemStackEvent {
    public boolean overrideShears;
}