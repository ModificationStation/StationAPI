package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.SimpleEvent;

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

    SimpleEvent<PreInit> EVENT = new SimpleEvent<>(PreInit.class, listeners ->
            () -> {
        for (PreInit event : listeners)
            event.preInit();
    });

    void preInit();
}
