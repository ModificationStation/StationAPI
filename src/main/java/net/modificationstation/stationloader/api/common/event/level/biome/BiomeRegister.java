package net.modificationstation.stationloader.api.common.event.level.biome;

import net.modificationstation.stationloader.api.common.event.SimpleEvent;

import java.util.function.Consumer;

public interface BiomeRegister {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<BiomeRegister> EVENT = new SimpleEvent<>(BiomeRegister.class,
            listeners ->
                    () -> {
                        for (BiomeRegister listener : listeners)
                            listener.registerBiomes();
                    }, (Consumer<SimpleEvent<BiomeRegister>>) biomeRegister ->
            biomeRegister.register(() -> SimpleEvent.EVENT_BUS.post(new Data()))
    );

    void registerBiomes();

    final class Data extends SimpleEvent.Data<BiomeRegister> {

        private Data() {
            super(EVENT);
        }
    }
}
