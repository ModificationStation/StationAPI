package net.modificationstation.stationloader.api.common.event.mod;

import net.modificationstation.stationloader.api.common.event.ModEvent;

public interface PostInit {

    ModEvent<PostInit> EVENT = new ModEvent<>(PostInit.class,
            listeners ->
                    () -> {
                        for (PostInit listener : listeners)
                            listener.postInit();
                    },
            listener ->
                    () -> {
                        PostInit.EVENT.setCurrentListener(listener);
                        listener.postInit();
                        PostInit.EVENT.setCurrentListener(null);
                    },
            postInit ->
                    postInit.register(() -> ModEvent.post(new Data()), null)
    );

    void postInit();

    final class Data extends ModInitData<PostInit> {

        private Data() {
            super(EVENT);
        }
    }
}
