package net.modificationstation.stationloader.api.common.event.entity;

import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;
import org.apache.logging.log4j.util.TriConsumer;

public interface EntityRegister {

    Event<EntityRegister> EVENT = EventFactory.INSTANCE.newEvent(EntityRegister.class, (listeners) ->
            (registerID) -> {
                for (EntityRegister event : listeners)
                    event.registerEntities(registerID);
            });

    void registerEntities(TriConsumer<Class<? extends EntityBase>, String, Integer> registerID);
}
