package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.EventRegistry;
import net.modificationstation.stationloader.api.common.event.ModEvent;
import net.modificationstation.stationloader.api.common.registry.ModID;

/**
 * Event called before Minecraft launch
 * <p>
 * args: none
 * return: void
 *
 * @author mine_diver
 */

public interface PreInit {

    ModEvent<PreInit> EVENT = new ModEvent<>(PreInit.class,
            listeners ->
                    (eventRegistry, modID) -> {
                        for (PreInit listener : listeners)
                            listener.preInit(eventRegistry, PreInit.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (eventRegistry, modID) -> {
                        PreInit.EVENT.setCurrentListener(listener);
                        listener.preInit(eventRegistry, modID);
                        PreInit.EVENT.setCurrentListener(null);
                    }
    );

    void preInit(EventRegistry eventRegistry, ModID modID);
}
