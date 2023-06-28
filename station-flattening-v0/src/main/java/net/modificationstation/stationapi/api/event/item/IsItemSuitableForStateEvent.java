package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public final class IsItemSuitableForStateEvent extends Event {
    public final ItemInstance itemStack;
    public final BlockState state;
    public boolean suitable;
}
