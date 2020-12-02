package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.SimpleEvent;

public interface Init {

    SimpleEvent<Init> EVENT = new SimpleEvent<>(Init.class, listeners ->
            () -> {
                for (Init event : listeners)
                    event.init();
            });

    void init();
}
