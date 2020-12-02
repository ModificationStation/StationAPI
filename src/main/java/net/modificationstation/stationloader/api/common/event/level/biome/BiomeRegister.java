package net.modificationstation.stationloader.api.common.event.level.biome;

import net.modificationstation.stationloader.api.common.event.SimpleEvent;

public interface BiomeRegister {

    SimpleEvent<BiomeRegister> EVENT = new SimpleEvent<>(BiomeRegister.class, listeners ->
            () -> {
                for (BiomeRegister event : listeners)
                    event.registerBiomes();
            });

    void registerBiomes();
}
