package net.modificationstation.stationapi.api.common.event.entity;

import lombok.RequiredArgsConstructor;
import net.minecraft.entity.EntityBase;
import net.modificationstation.stationapi.api.common.event.Event;
import uk.co.benjiweber.expressions.functions.TriConsumer;

@RequiredArgsConstructor
public class EntityRegister extends Event {

    public final TriConsumer<Class<? extends EntityBase>, String, Integer> register;

    public final void register(Class<? extends EntityBase> entityClass, String entityIdentifier, int entityId) {
        register.accept(entityClass, entityIdentifier, entityId);
    }
}
