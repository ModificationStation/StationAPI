package net.modificationstation.stationloader.api.common.event.entity;

import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;
import uk.co.benjiweber.expressions.functions.TriConsumer;

public interface EntityRegister {

    SimpleEvent<EntityRegister> EVENT = new SimpleEvent<>(EntityRegister.class, listeners ->
            register -> {
                for (EntityRegister event : listeners)
                    event.registerEntities(register);
            });

    void registerEntities(TriConsumer<Class<? extends EntityBase>, String, Integer> register);
}
