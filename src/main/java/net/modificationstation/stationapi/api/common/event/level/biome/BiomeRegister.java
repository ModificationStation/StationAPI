package net.modificationstation.stationapi.api.common.event.level.biome;

import net.modificationstation.stationapi.api.common.event.GameEvent;

import java.util.function.Consumer;

public interface BiomeRegister {

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