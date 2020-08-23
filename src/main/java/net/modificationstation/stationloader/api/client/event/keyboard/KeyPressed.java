package net.modificationstation.stationloader.api.client.event.keyboard;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

public interface KeyPressed {

    Event<KeyPressed> EVENT = EventFactory.INSTANCE.newEvent(KeyPressed.class, (listeners) ->
            () -> {
                for (KeyPressed event : listeners)
                    event.keyPressed();
            });

    void keyPressed();
}
