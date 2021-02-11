package net.modificationstation.stationapi.api.common.event.level.biome;

import net.modificationstation.stationapi.api.common.event.GameEventOld;

import java.util.function.Consumer;

public interface BiomeRegister {

    GameEventOld<BiomeRegister> EVENT = new GameEventOld<>(BiomeRegister.class,
            listeners ->
                    () -> {
                        for (BiomeRegister listener : listeners)
                            listener.registerBiomes();
                    },
            (Consumer<GameEventOld<BiomeRegister>>) biomeRegister ->
                    biomeRegister.register(() -> GameEventOld.EVENT_BUS.post(new Data()))
    );

    void registerBiomes();

    final class Data extends GameEventOld.Data<BiomeRegister> {

        private Data() {
            super(EVENT);
        }
    }
}
