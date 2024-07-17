package net.modificationstation.stationapi.api.event.registry;

import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;

/**
 * This is the last init event called by StAPI.
 * Called after registries are frozen.
 * {@link net.modificationstation.stationapi.mixin.registry.client.MinecraftMixin}
 */
@SuppressWarnings("JavadocReference") // Shut, I know.
@EventPhases(StationAPI.INTERNAL_PHASE)
public class RegistriesFrozenEvent extends Event {

}
