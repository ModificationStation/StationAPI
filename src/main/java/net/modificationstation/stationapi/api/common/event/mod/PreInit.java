package net.modificationstation.stationapi.api.common.event.mod;

import lombok.Getter;
import net.modificationstation.stationapi.api.common.event.EventRegistry;
import net.modificationstation.stationapi.api.common.event.ModEvent;
import net.modificationstation.stationapi.api.common.registry.ModID;

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
                    (registry, modID) -> {
                        for (PreInit listener : listeners)
                            listener.preInit(registry, PreInit.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (registry, modID) -> {
                        PreInit.EVENT.setCurrentListener(listener);
                        listener.preInit(registry, modID);
                        PreInit.EVENT.setCurrentListener(null);
                    },
            preInit ->
                    preInit.register((registry, modID) -> ModEvent.post(new Data(registry)), null)
    );

    void preInit(EventRegistry eventRegistry, ModID modID);

    final class Data extends ModInitData<PreInit> {

        @Getter
        private final EventRegistry registry;

        private Data(EventRegistry registry) {
            super(EVENT);
            this.registry = registry;
        }
    }
}
