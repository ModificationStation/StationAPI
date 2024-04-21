package net.modificationstation.stationapi.api.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.Cancelable;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;

/**
 * Event that gets called when dispenser is activated, precedes and overrides CustomDispenseBehavior.
 * @author matthewperiut
 * @see CustomDispenseBehavior
 * @see ItemDispenseContext
 */
@Cancelable
@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class DispenseEvent extends Event {
    public final ItemDispenseContext itemDispenseContext;
}
