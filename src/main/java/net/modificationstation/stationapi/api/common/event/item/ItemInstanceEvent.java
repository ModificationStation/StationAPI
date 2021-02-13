package net.modificationstation.stationapi.api.common.event.item;

import lombok.RequiredArgsConstructor;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationapi.api.common.event.Event;

@RequiredArgsConstructor
public class ItemInstanceEvent extends Event {

    public final ItemInstance itemInstance;
}
