package net.modificationstation.stationapi.api.client.event.color.block;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public class BlockColorsRegisterEvent extends Event {
    public final BlockColors blockColors;
}
