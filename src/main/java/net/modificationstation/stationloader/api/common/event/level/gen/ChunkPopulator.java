package net.modificationstation.stationloader.api.common.event.level.gen;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

public interface ChunkPopulator {

    Event<ChunkPopulator> EVENT = EventFactory.INSTANCE.newEvent(ChunkPopulator.class, (listeners) ->
            () -> {
                for (ChunkPopulator event : listeners)
                    event.populate();
            });

    void populate();
}
