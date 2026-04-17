package net.modificationstation.stationapi.api.event.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;

@SuperBuilder
public class EntityRegisterEvent extends Event {
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

    public final void register(String entityIdentifier, int entityId, Class<? extends Entity> entityClass) {
        register.register(entityClass, entityIdentifier, entityId);
    }

    public final void register(String entityIdentifier, Class<? extends Entity> entityClass) {
        registerNoID.register(entityClass, entityIdentifier);
    }
    
    public final void register(Identifier entityIdentifier, Class<? extends Entity> entityClass) {
        registerNoID.register(entityClass, entityIdentifier.toString());
    }
}
