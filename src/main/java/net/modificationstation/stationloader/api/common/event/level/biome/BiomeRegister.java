package net.modificationstation.stationloader.api.common.event.level.biome;

import net.modificationstation.stationloader.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface BiomeRegister {

    @SuppressWarnings("UnstableApiUsage")
    GameEvent<BiomeRegister> EVENT = new GameEvent<>(BiomeRegister.class,
            listeners ->
                    () -> {
                        for (BiomeRegister listener : listeners)
                            listener.registerBiomes();
                    },
            (Consumer<GameEvent<BiomeRegister>>) biomeRegister ->
                    biomeRegister.register(() -> GameEvent.EVENT_BUS.post(new Data()))
    );

    void registerBiomes();

    final class Data extends GameEvent.Data<BiomeRegister> {

        private Data() {
            super(EVENT);
        }
    }
}
