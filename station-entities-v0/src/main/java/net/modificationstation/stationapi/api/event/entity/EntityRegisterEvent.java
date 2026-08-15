package net.modificationstation.stationapi.api.event.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;

@SuperBuilder
public class EntityRegisterEvent extends Event {
    @FunctionalInterface
    public interface RegisterFunction {
        void register(Class<? extends Entity> entityClass, Identifier entityIdentifier);
    }

    public final RegisterFunction register;

    public final void register(Identifier entityIdentifier, Class<? extends Entity> entityClass) {
        register.register(entityClass, entityIdentifier);
    }
}
