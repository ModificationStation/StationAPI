package net.modificationstation.stationloader.common.event.item;

import net.modificationstation.stationloader.common.event.Event;
import net.modificationstation.stationloader.common.event.StationEvent;

public interface ItemRegister {

    Event<ItemRegister> EVENT = new StationEvent<>(ItemRegister.class, (listeners) ->
            () -> {
                for (ItemRegister event : listeners)
                    event.registerItems();
            });

    void registerItems();
}
