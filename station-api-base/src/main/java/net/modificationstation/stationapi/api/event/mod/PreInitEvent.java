package net.modificationstation.stationapi.api.event.mod;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;

/**
 * PreInitialization event called for mods to do some stuff right after the preLaunch and StAPI setup.
 * Some additional setup can be done as well, but Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class PreInitEvent extends Event {}
