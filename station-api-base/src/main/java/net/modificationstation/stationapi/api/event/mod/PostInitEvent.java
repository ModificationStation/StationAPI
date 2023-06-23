package net.modificationstation.stationapi.api.event.mod;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;

/**
 * PostInitialization event called for mods to do their setup when the other mods finished initializing.
 * Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
@SuperBuilder
public class PostInitEvent extends Event {}
