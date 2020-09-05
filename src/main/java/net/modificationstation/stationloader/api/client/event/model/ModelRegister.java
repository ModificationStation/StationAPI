package net.modificationstation.stationloader.api.client.event.model;

import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.event.EventFactory;

public interface ModelRegister {

    enum Type {

        BLOCKS,
        ITEMS,
        ENTITIES
    }

    Event<ModelRegister> EVENT = EventFactory.INSTANCE.newEvent(ModelRegister.class, (listeners) ->
            (type) -> {
                for (ModelRegister event : listeners)
                    event.registerModels(type);
            });

    void registerModels(Type type);
}
