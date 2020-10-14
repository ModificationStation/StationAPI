package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

public interface Init {

    Event<Init> EVENT = EventFactory.INSTANCE.newEvent(Init.class, (listeners) ->
            () -> {
                for (Init event : listeners)
                    event.init();
            });

    void init();
}
