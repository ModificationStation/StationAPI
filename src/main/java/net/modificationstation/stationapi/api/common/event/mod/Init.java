package net.modificationstation.stationapi.api.common.event.mod;

import net.modificationstation.stationapi.api.common.event.ModEventOld;
import net.modificationstation.stationapi.api.common.registry.ModID;

/**
 * Initialization event called for mods to mostly just register event listeners, since the events are already done in {@link PreInit}, or load the config.
 * Some additional setup can be done as well, but Minecraft classes can not be referenced during this event.
 * @author mine_diver
 */
public interface Init {

    /**
     * The event instance.
     */
    ModEventOld<Init> EVENT = new ModEventOld<>(Init.class,
            listeners ->
                    modID -> {
                        for (Init listener : listeners)
                            listener.init(Init.EVENT.getListenerModID(listener));
                    },
            listener ->
                    modID -> {
                        Init.EVENT.setCurrentListener(listener);
                        listener.init(modID);
                        Init.EVENT.setCurrentListener(null);
                    },
            init ->
                    init.register(modID -> ModEventOld.post(new Data()), null)
    );

    /**
     * The event function.
     * @param modID current listener's ModID.
     */
    void init(ModID modID);

    /**
     * The event data used by EventBus.
     */
    final class Data extends ModInitData<Init> {

        private Data() {
            super(EVENT);
        }
    }
}
