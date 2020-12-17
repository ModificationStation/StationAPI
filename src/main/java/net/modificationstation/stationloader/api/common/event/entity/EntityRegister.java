package net.modificationstation.stationloader.api.common.event.entity;

import lombok.Getter;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationloader.api.common.entity.EntityHandlerRegistry;
import net.modificationstation.stationloader.api.common.event.ModEvent;
import net.modificationstation.stationloader.api.common.registry.ModID;
import uk.co.benjiweber.expressions.functions.TriConsumer;

public interface EntityRegister {

    @SuppressWarnings("UnstableApiUsage")
    ModEvent<EntityRegister> EVENT = new ModEvent<>(EntityRegister.class,
            listeners ->
                    (register, registry, modID) -> {
                        for (EntityRegister listener : listeners)
                            listener.registerEntities(register, registry, EntityRegister.EVENT.getListenerModID(listener));
                    },
            listener ->
                    (register, registry, modID) -> {
                        EntityRegister.EVENT.setCurrentListener(listener);
                        listener.registerEntities(register, registry, modID);
                        EntityRegister.EVENT.setCurrentListener(null);
                    },
            entityRegister ->
                    entityRegister.register((register, registry, modID) -> ModEvent.post(new Data(register, registry)), null)
    );

    void registerEntities(TriConsumer<Class<? extends EntityBase>, String, Integer> register, EntityHandlerRegistry registry, ModID modID);

    final class Data extends ModEvent.Data<EntityRegister> {

        private final TriConsumer<Class<? extends EntityBase>, String, Integer> register;
        @Getter
        private final EntityHandlerRegistry registry;

        private Data(TriConsumer<Class<? extends EntityBase>, String, Integer> register, EntityHandlerRegistry registry) {
            super(EVENT);
            this.register = register;
            this.registry = registry;
        }

        public void register(Class<? extends EntityBase> entityClass, String entityIdentifier, int entityId) {
            register.accept(entityClass, entityIdentifier, entityId);
        }
    }
}
