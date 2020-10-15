package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

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

    Event<PreInit> EVENT = EventFactory.INSTANCE.newEvent(PreInit.class, (listeners) ->
            () -> {
        for (PreInit event : listeners)
            event.preInit();
    });

    void preInit();
}
