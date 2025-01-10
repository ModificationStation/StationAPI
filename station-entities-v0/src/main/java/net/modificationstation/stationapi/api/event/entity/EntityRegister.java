package net.modificationstation.stationapi.api.event.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.Entity;

@SuperBuilder
public class EntityRegister extends Event {
    @FunctionalInterface
    public interface RegisterFunction {
        void register(Class<? extends Entity> entityClass, String entityIdentifier, int entityId);
    }

    @FunctionalInterface
    public interface RegisterFunctionNoId {
        void register(Class<? extends Entity> entityClass, String entityIdentifier);
    }

    public final RegisterFunction register;
    public final RegisterFunctionNoId registerNoID;

    public final void register(Class<? extends Entity> entityClass, String entityIdentifier, int entityId) {
        register.register(entityClass, entityIdentifier, entityId);
    }

    public final void register(Class<? extends Entity> entityClass, String entityIdentifier) {
        registerNoID.register(entityClass, entityIdentifier);
    }
}
