package net.modificationstation.stationapi.api.event.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.Entity;
import uk.co.benjiweber.expressions.function.TriConsumer;

import java.util.function.BiConsumer;

@SuperBuilder
public class EntityRegister extends Event {
    public final TriConsumer<Class<? extends Entity>, String, Integer> register;
    public final BiConsumer<Class<? extends Entity>, String> registerNoID;

    public final void register(Class<? extends Entity> entityClass, String entityIdentifier, int entityId) {
        register.accept(entityClass, entityIdentifier, entityId);
    }

    public final void register(Class<? extends Entity> entityClass, String entityIdentifier) {
        registerNoID.accept(entityClass, entityIdentifier);
    }
}
