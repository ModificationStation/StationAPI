package net.modificationstation.stationapi.api.common.event.mod;

import net.modificationstation.stationapi.api.common.event.Event;

/**
 * Initialization event called for mods to mostly just register event listeners, since the events are already done in {@link PreInit}, or load the config.
 * Some additional setup can be done as well, but Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
public class Init extends Event {


}
