package net.modificationstation.stationapi.api.event.datafixer;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class DataFixerRegisterEvent extends Event {}
