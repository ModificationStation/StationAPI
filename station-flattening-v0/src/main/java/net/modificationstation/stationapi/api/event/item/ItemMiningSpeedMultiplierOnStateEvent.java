package net.modificationstation.stationapi.api.event.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.block.BlockState;

@SuperBuilder
@EventPhases(StationAPI.INTERNAL_PHASE)
public final class ItemMiningSpeedMultiplierOnStateEvent extends Event {
    public final ItemStack itemStack;
    public final BlockState state;
    public float miningSpeedMultiplier;
}
