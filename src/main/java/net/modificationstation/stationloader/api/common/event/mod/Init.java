package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.GameEvent;

public interface Init {

    GameEvent<Init> EVENT = new GameEvent<>(Init.class,
            listeners ->
                    () -> {
                        for (Init event : listeners)
                            event.init();
                    }
    );

    void init();
}
