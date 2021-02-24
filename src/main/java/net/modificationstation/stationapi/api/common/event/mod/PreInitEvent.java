package net.modificationstation.stationapi.api.common.event.mod;

import net.modificationstation.stationapi.api.common.event.Event;

/**
 * PreInitialization event called for mods to do some stuff right after the preLaunch and StAPI setup.
 * Some additional setup can be done as well, but Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
public class PreInitEvent extends Event { }
