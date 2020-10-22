package net.modificationstation.stationloader.api.common.event.item;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface ItemRegister {

    Event<ItemRegister> EVENT = EventFactory.INSTANCE.newEvent(ItemRegister.class, listeners ->
            () -> {
                for (ItemRegister event : listeners)
                    event.registerItems();
            });

    void registerItems();
}
