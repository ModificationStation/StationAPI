package net.modificationstation.stationapi.api.event.entity;

import lombok.experimental.SuperBuilder;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.entity.EntityBase;
import uk.co.benjiweber.expressions.function.TriConsumer;

import java.util.function.BiConsumer;

@SuperBuilder
public class EntityRegister extends Event {

    public final TriConsumer<Class<? extends EntityBase>, String, Integer> register;
    public final BiConsumer<Class<? extends EntityBase>, String> registerNoID;

    public final void register(Class<? extends EntityBase> entityClass, String entityIdentifier, int entityId) {
        register.accept(entityClass, entityIdentifier, entityId);
    }

    public final void register(Class<? extends EntityBase> entityClass, String entityIdentifier) {
        registerNoID.accept(entityClass, entityIdentifier);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
