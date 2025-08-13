package net.modificationstation.stationapi.api.client.event.color.item;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.client.color.block.BlockColors;
import net.modificationstation.stationapi.api.client.color.item.ItemColors;

@SuperBuilder
public class ItemColorsRegisterEvent extends Event {
    public final BlockColors blockColors;
    public final ItemColors itemColors;
}
