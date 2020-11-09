package net.modificationstation.stationloader.api.common.event.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface ItemNameSet {

    Event<ItemNameSet> EVENT = EventFactory.INSTANCE.newEvent(ItemNameSet.class, listeners ->
            (itemBase, name) -> {
                for (ItemNameSet event : listeners)
                    name = event.getName(itemBase, name);
                return name;
            });

    String getName(ItemBase itemBase, String name);
}
