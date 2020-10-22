package net.modificationstation.stationloader.api.common.event.entity;

import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.common.event.Event;
import net.modificationstation.stationloader.api.common.factory.EventFactory;
import uk.co.benjiweber.expressions.functions.TriConsumer;

public interface EntityRegister {

    Event<EntityRegister> EVENT = EventFactory.INSTANCE.newEvent(EntityRegister.class, listeners ->
            register -> {
                for (EntityRegister event : listeners)
                    event.registerEntities(register);
            });

    void registerEntities(TriConsumer<Class<? extends EntityBase>, String, Integer> register);
}
