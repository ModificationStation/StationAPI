package net.modificationstation.stationloader.api.common.event.level.biome;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;

public interface BiomeRegister {

    Event<BiomeRegister> EVENT = EventFactory.INSTANCE.newEvent(BiomeRegister.class, listeners ->
            () -> {
                for (BiomeRegister event : listeners)
                    event.registerBiomes();
            });

    void registerBiomes();
}
