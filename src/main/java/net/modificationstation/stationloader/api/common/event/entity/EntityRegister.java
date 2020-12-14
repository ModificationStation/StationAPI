package net.modificationstation.stationloader.api.common.event.entity;

import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.common.event.SimpleEvent;
import uk.co.benjiweber.expressions.functions.TriConsumer;

import java.util.function.Consumer;

public interface EntityRegister {

    @SuppressWarnings("UnstableApiUsage")
    SimpleEvent<EntityRegister> EVENT = new SimpleEvent<>(EntityRegister.class,
            listeners ->
                    register -> {
        for (EntityRegister listener : listeners)
            listener.registerEntities(register);
    }, (Consumer<SimpleEvent<EntityRegister>>) entityRegister ->
            entityRegister.register(register -> SimpleEvent.EVENT_BUS.post(new Data(register)))
    );

    void registerEntities(TriConsumer<Class<? extends EntityBase>, String, Integer> register);

    final class Data extends SimpleEvent.Data<EntityRegister> {

        private Data(TriConsumer<Class<? extends EntityBase>, String, Integer> register) {
            super(EVENT);
            this.register = register;
        }

        public void register(Class<? extends EntityBase> entityClass, String entityIdentifier, int entityId) {
            register.accept(entityClass, entityIdentifier, entityId);
        }

        private final TriConsumer<Class<? extends EntityBase>, String, Integer> register;
    }
}
