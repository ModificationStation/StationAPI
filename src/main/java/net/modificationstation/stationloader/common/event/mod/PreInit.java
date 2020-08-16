package net.modificationstation.stationloader.common.event.mod;

import net.modificationstation.stationloader.common.event.Event;
import net.modificationstation.stationloader.common.event.StationEvent;

/**
 * Event called before Minecraft launch
 *
 * args: none
 * return: void
 *
 * @author mine_diver
 *
 */

public interface PreInit {

    Event<PreInit> EVENT = new StationEvent<>(PreInit.class, (listeners) ->
            () -> {
        for (PreInit event : listeners)
            event.preInit();
    });

    void preInit();
}
