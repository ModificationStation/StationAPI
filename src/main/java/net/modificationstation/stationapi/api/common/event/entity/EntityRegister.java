package net.modificationstation.stationapi.api.common.event.entity;

import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.common.entity.EntityHandlerRegistry;
import net.modificationstation.stationapi.api.common.event.registry.RegistryEvent;
import uk.co.benjiweber.expressions.functions.TriConsumer;

public class EntityRegister extends RegistryEvent<EntityHandlerRegistry> {

    public final TriConsumer<Class<? extends EntityBase>, String, Integer> register;

    public EntityRegister(TriConsumer<Class<? extends EntityBase>, String, Integer> register) {
        super(EntityHandlerRegistry.INSTANCE);
        this.register = register;
    }

    public void register(Class<? extends EntityBase> entityClass, String entityIdentifier, int entityId) {
        register.accept(entityClass, entityIdentifier, entityId);
    }
}
