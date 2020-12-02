package net.modificationstation.stationloader.api.common.event.item;

import net.minecraft.item.ItemBase;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;

public interface ItemNameSet {

    SimpleEvent<ItemNameSet> EVENT = new SimpleEvent<>(ItemNameSet.class, listeners ->
            (itemBase, name) -> {
                for (ItemNameSet event : listeners)
                    name = event.getName(itemBase, name);
                return name;
            });

    String getName(ItemBase itemBase, String name);
}
