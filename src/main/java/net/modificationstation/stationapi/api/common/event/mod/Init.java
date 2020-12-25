package net.modificationstation.stationapi.api.common.event.mod;

import net.modificationstation.stationapi.api.common.event.ModEvent;

public interface Init {

    ModEvent<Init> EVENT = new ModEvent<>(Init.class,
            listeners ->
                    () -> {
                        for (Init event : listeners)
                            event.init();
                    },
            listener ->
                    () -> {
                        Init.EVENT.setCurrentListener(listener);
                        listener.init();
                        Init.EVENT.setCurrentListener(null);
                    },
            init ->
                    init.register(() -> ModEvent.post(new Data()), null)
    );

    void init();

    final class Data extends ModInitData<Init> {

        private Data() {
            super(EVENT);
        }
    }
}
