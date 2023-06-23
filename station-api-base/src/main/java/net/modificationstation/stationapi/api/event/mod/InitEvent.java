package net.modificationstation.stationapi.api.event.mod;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;

/**
 * Initialization event called for mods to mostly just register event listeners, since the events are already done in {@link PreInitEvent}, or load the config.
 * Some additional setup can be done as well, but Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class InitEvent extends Event {}
